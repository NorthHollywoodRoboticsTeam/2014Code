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
    Port description:
    Electro magnet: Relay port on sidecar 1
    Feeder: Relay Port 2 and 3 (Reverse as needed)
    Winch: PWM Port 5
    Drive: PWM ports 1-4 AS LABELED ON MOTORS and with the Jags the right way.
    */
    
    
    RobotDrive drive = new RobotDrive(new Jaguar(2),new Jaguar(1),new InvertedSpeedControler(new Jaguar(3)),new InvertedSpeedControler(new Jaguar(4)));
    Jaguar winch1 = new Jaguar(5), winch2 = new Jaguar(6);
    Relay feeder1 = new Relay(2), feeder2 = new Relay(3);
    Relay  electroMagnet = new Relay(1);
    Joystick js1 = new Joystick(1), js2 = new Joystick(2);
    
    
    NetworkTable server = NetworkTable.getTable("SmartDashboard");
    
    /**
     * 
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
        System.out.println("starting en.");
        while (isOperatorControl() && isEnabled()) {
            try {
                if (((Double)(server.getValue("BLOB_COUNT"))).intValue() >= 2) {
                System.out.println("shoot NOW");
                }else{ 
                System.out.println("Don't shoot");
            }
            } catch (Exception ex) {
                
            }
            if (js1.getRawButton(11)) {
                winch1.set(1);
                winch2.set(-1);
            }
            else if (js1.getRawButton(10)) {
                winch1.set(-1);
                winch2.set(1);
            }
            else {
             
                winch1.set(0);
                winch2.set(0);
            }
            if (js1.getTrigger()) {
                electroMagnet.set(Relay.Value.kOff);
            } else {
                electroMagnet.set(Relay.Value.kForward);
            }
            if (js1.getRawButton(7)) {
                feeder1.set(Relay.Value.kOff);
                feeder2.set(Relay.Value.kOff);
            } else {
                feeder1.set(Relay.Value.kForward);
                feeder2.set(Relay.Value.kReverse);
            }
            drive.mecanumDrive_Polar(js1.getMagnitude(), js1.getDirectionDegrees(), js2.getX());
        }
    }
    
    /**
     * This function is called once each time the robot enters test mode.
     */
    
}
