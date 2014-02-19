/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends SimpleRobot {

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void autonomous() {

    }

    /*
     Joystick spec.
     Driving: js1 Y for forward and backward, js1 X for turn and js2 X for sideways
     Winching: js3 up and down on knob
     Feeding: Toggle js2 10 for on and off and js2 11 overrides that and dose reverse
     Shooting: js3 button 2 turns it on and js3 trigger resets it
     */
    /*
     Port description:
     Electro magnet: Relay port on sidecar 1
     Feeder: Relay Port 2 and 3 (Reverse as needed)
     Winch: PWM Port 5 and 6
     Drive: PWM ports 1-4 AS LABELED ON MOTORS and with the Jags the right way.
     */
    RobotDrive drive = new RobotDrive(new Jaguar(2), new Jaguar(1), new InvertedSpeedControler(new Jaguar(3)), new InvertedSpeedControler(new Jaguar(4)));

    Jaguar winch1 = new Jaguar(5), winch2 = new Jaguar(6);
    Relay feeder1 = new Relay(2), feeder2 = new Relay(3);
    Relay electroMagnet = new Relay(1);
    Joystick js1 = new Joystick(1), js2 = new Joystick(2), js3 = new Joystick(3);

    //js 3 ax 5  is x and 6 is Y
    //NetworkTable server = NetworkTable.getTable("SmartDashboard");
    boolean button3State = false, button10State = false;

    boolean feederOn = false;

    /**
     *
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        System.out.println("starting en.");
        while (isOperatorControl() && isEnabled()) {
            /*try {
             if (((Double)(server.getValue("BLOB_COUNT"))).intValue() >= 2) {
             System.out.println("shoot NOW");
             }else{ 
             System.out.println("Don't shoot");
             }
             } catch (Exception ex) {
                
             }*/

            winch1.set(js3.getRawAxis(6));
            winch2.set(-js3.getRawAxis(6));
            if (!js3.getRawButton(2)) {
                button3State = false;
            }
            if (js3.getRawButton(2) == true && button3State == false) {
                button3State = true;
                electroMagnet.set(Relay.Value.kForward);
            }

            if (js3.getTrigger()) {
                electroMagnet.set(Relay.Value.kOff);
            }
            if (js2.getRawButton(10) != button10State) {
                if (js2.getRawButton(10)) {
                    if (!feederOn) {
                        feederOn = true;
                    } else {
                        feederOn = false;
                    }
                    button10State = true;
                } else {
                    button10State = false;
                }
            }
            if (js3.getRawButton(11)) {
                //System.out.println("reverse");
                feeder1.set(Relay.Value.kReverse);
                feeder2.set(Relay.Value.kReverse);
            } else {
                if (js3.getRawAxis(4) < .5) {

                    feeder1.set(Relay.Value.kForward);
                    feeder2.set(Relay.Value.kForward);
                } else {

                    feeder1.set(Relay.Value.kOff);
                    feeder2.set(Relay.Value.kOff);
                }
            }

            drive.mecanumDrive_Cartesian(js1.getY(), js2.getX(), js1.getX() / 2, 0);
            //drive.mecanumDrive_Polar(js1.getMagnitude(), js1.getDirectionDegrees() - 90, js2.getX());
        }
    }

    /**
     * This function is called once each time the robot enters test mode.
     */
}
