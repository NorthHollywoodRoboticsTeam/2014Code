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

    /**
     * 
     * This function is called once each time the robot enters operator control.
     */
    RobotDrive drive = new RobotDrive(new Jaguar(2),new Jaguar(1),new InvertedSpeedControler(new Jaguar(3)),new InvertedSpeedControler(new Jaguar(4)));
     
    Relay  relay = new Relay(1);
    Joystick js1 = new Joystick(1), js2 = new Joystick(2);
    public void operatorControl() {
        System.out.println("starting en.");
        while (isOperatorControl() && isEnabled()) {
            drive.mecanumDrive_Polar(js1.getMagnitude(), js1.getDirectionDegrees() + 90, js2.getX());
            //drive.mecanumDrive_Cartesian(js1.getX(), js1.getY(), js2.getX(), js2.getY());
            //operatorControl();
        }
    }
    
    /**
     * This function is called once each time the robot enters test mode.
     */
    
}
