/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.networktables2.server.NetworkTableServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
    
    //NetworkTable server = NetworkTable.getTable("SmartDashboard");
    public void autonomous() {
        System.out.println("driving positive");
        
        mecDrive(.5, 0, 0);
        Timer.delay(3.2);
        mecDrive(0, 0, 0);
       /*drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
       drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
       drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        
        //Timer.delay(7);
        
        drive.setSafetyEnabled(false);
        
            drive.arcadeDrive(-.5,0);
        
        Timer.delay(6);
        drive.arcadeDrive(0,0);*/
        /*(new Thread(new Runnable() {

            public void run() {
                
                //Var initialization                
                long winchingStartTime = -1;
                boolean isWinching = false;
                
                

                //true = winching down false = winching up
                boolean autoWinchDirection = false;
                
                //winch down
                winchingStartTime = System.currentTimeMillis();
                isWinching = true;
                autoWinchDirection = true;
                electroMagnet.set(Relay.Value.kForward);
                winchForward();
                
                
                
                //check conditions
                while (isAutonomous() || isEnabled()) {
                    if (winchingStartTime != -1 && isWinching) {
                        if (((autoWinchDirection == false) && System.currentTimeMillis() - winchingStartTime > winchingTimeUp)
                                || ((autoWinchDirection == true) && System.currentTimeMillis() - winchingStartTime > winchingTimeDown)) {
                            if (autoWinchDirection == true) {
                                winchingStartTime = System.currentTimeMillis();
                                isWinching = true;
                                autoWinchDirection = false;
                                winchReverse();
                                System.out.println("reversing winch and winching up");
                            } else {
                                winchingStartTime = -1;
                                isWinching = false;
                                autoWinchDirection = true;
                                winchStop();
                                System.out.println("Stopping winch and shooting");
                                break;
                            }
                        }
                    }

                    if (!limitSwitch.get() && isWinching) {
                        if (autoWinchDirection == true) {
                            winchingStartTime
                                    = (System.currentTimeMillis() + winchLimitSwitchOverload) //The system time we want the winch to stop at.
                                    - winchingTimeDown;  //Minos the time it should winch down = the winch will stop in winchLimitSwitchOverload.
                        }
                    }
                }
                
                Timer.delay(1);
                
                //Shoot!!!!!
                System.out.println("downwinch compelte and shooting");
                electroMagnet.set(Relay.Value.kOff);

            }
        })).start();*/
        
        
        
        
        //No need to shoot here, the shot will be done on the winch thread.
        
        
    }

    
    /*
     Port description:
     Electro magnet: Relay port on sidecar 1
     Feeder: Relay Port 2 and 3 (Reverse as needed)
     Winch: PWM Port 5 and 6
     Drive: PWM ports 1-4 AS LABELED ON MOTORS and with the Jags the right way.
     */
    //FL RL  FR RR
    //RobotDrive drive = new RobotDrive(new Jaguar(4), new Jaguar(3), new Jaguar(1), new Jaguar(2));
    
    
    Jaguar frontLeft = new Jaguar(4), rearLeft = new Jaguar(3), frontRight = new Jaguar(1), rearRight = new Jaguar(2);
    
    
    Joystick js3 = new Joystick(3), js1 = new Joystick(1);
    
    SpeedController buren1 = new Victor(5);
    SpeedController buren2 = new Jaguar(6);
    Relay feeder1 = new Relay(2), feeder2 = new Relay(3);
    Relay electroMagnet = new Relay(1);
    
    DigitalInput limitSwitch = new DigitalInput(1);

    boolean electroMagnetOnButtonState = false;

    JoystickLayout joystickLayout = new AllInOne(new Joystick(1), new Joystick(2), new Joystick(3));
    
    boolean feederOn = false;

    public boolean button7State = false;
    
    public final long winchingTimeUp = (long) (3.5 * 1000);
    public final long winchingTimeDown = (long) (3.56 * 1000);

    private final long winchLimitSwitchOverload = (long) (10);

    //the throttle is axis 3!!!!!!!!!!!
    /**
     *
     * This function is called once each time the robot enters operator control.
     */
    
    
    public double clamp(double val) {
        if (val > 1) {
            return 1;
        } else if (val < -1) {
            return -1;
        } else {
            return val;
        }
    }
    public void mecDrive(double forward, double turnRight, double strafeRight) {
        double FL = clamp(forward + turnRight + strafeRight);
        double RL = clamp(forward + turnRight - strafeRight);
        double FR = clamp(forward - turnRight - strafeRight);
        double RR = clamp(forward - turnRight + strafeRight);
        frontLeft.set(FL);
        rearLeft.set(RL);
        frontRight.set(-FR);
        rearRight.set(-RR);
        
        
    }
    
    public void operatorControl() {
        //1, 3
        /*
            FL    FR
        
            RL    RR
        
            forward ->   +FL +RL +FR +RR
            turnright -> +FL +RL -FR -RR
            strafe ->    +FL -RL -FR +RR
        
            FL = (fwd + turn + strafe);
            RL = (fwd + turn - strafe);
            FR = (fwd - turn - strafe);
            RR = (fwd - turn + strafe);
        */
       /*drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
       drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
       drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);*/
        long winchingStartTime = -1;

        boolean isWinching = false;

        //true = winching down false = winching up
        boolean autoWinchDirection = false;
        System.out.println("starting en v2.");
        while (isOperatorControl() && isEnabled()) {
            //SmartDashboard.putBoolean("Limit Switch Status2:", limitSwitch.get());
            
            if (joystickLayout.autoWinchDown()) {
                winchingStartTime = System.currentTimeMillis();
                isWinching = true;
                autoWinchDirection = true;
                electroMagnet.set(Relay.Value.kForward);
                winchForward();
            }
            if (joystickLayout.autoWinchUp()) {
                winchingStartTime = System.currentTimeMillis();
                isWinching = true;
                autoWinchDirection = false;
                winchReverse();
            }

            if (winchingStartTime != -1 && isWinching) {
                if (((autoWinchDirection == false) && System.currentTimeMillis() - winchingStartTime > winchingTimeUp) || 
                        ((autoWinchDirection == true) && System.currentTimeMillis() - winchingStartTime > winchingTimeDown)) {
                    winchingStartTime = -1;
                    isWinching = false;
                    winchStop();
                }
            }
            if (joystickLayout.manualWinchDown() || joystickLayout.manualWinchUp()) {
                if (joystickLayout.manualWinchDown()) {
                    winchForward();
                } else {
                    winchReverse();
                }
                winchingStartTime = -1;
                isWinching = false;
            } else if (!isWinching) {
                winchStop();
            }

            if (limitSwitch.get() && isWinching) {
                if (autoWinchDirection == true) {
                    winchingStartTime = 
                            (System.currentTimeMillis() + winchLimitSwitchOverload)  //The system time we want the winch to stop at.
                            - winchingTimeDown;  //Minos the time it should winch down = the winch will stop in winchLimitSwitchOverload.
                }
            }
            if (joystickLayout.winchOff()) {
                winchingStartTime = -1;
                isWinching = false;
                winchStop();
            }

            
            if (joystickLayout.electroMagnetOn()) {
                electroMagnetOnButtonState = true;
                System.out.println("turning on electro magnet");
                electroMagnet.set(Relay.Value.kForward);
            }

            if (joystickLayout.electroMagnetReset()) {
                System.out.println("turning off e-mag");
                electroMagnet.set(Relay.Value.kOff);
            }
            
            if (joystickLayout.overrideFeederReverse()) {
                feeder1.set(Relay.Value.kReverse);
                feeder2.set(Relay.Value.kReverse);
            } else {
                if (joystickLayout.setFeederOn()) {

                    feeder1.set(Relay.Value.kForward);
                    feeder2.set(Relay.Value.kForward);
                } else {

                    feeder1.set(Relay.Value.kOff);
                    feeder2.set(Relay.Value.kOff);
                }
            }
             
            //drive.mecanumDrive_Cartesian(joystickLayout.driveForward(), joystickLayout.driveLeft(), joystickLayout.driveRotation(), 0);
            //drive.arcadeDrive(js3);
            //drive.arcadeDrive(js3.getY(),js3.getX());
            mecDrive(joystickLayout.driveForward(), joystickLayout.driveRotation(), joystickLayout.driveLeft());
        }
    }

    private void winchStop() {
        buren1.set(0);
        buren2.set(0);
    }

    private void winchForward() {
        buren1.set(1);
        buren2.set(-1 * .985);
    }

    private void winchReverse() {

        buren1.set(-1);
        buren2.set(1 * .985);
    }
    /**
     * This function is called once each time the robot enters test mode.
     */
}
