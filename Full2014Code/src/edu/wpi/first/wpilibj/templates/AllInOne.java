/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;

/*
     Joystick spec for all in one.
     Driving: js3 Y for forward and backward, js3 X for turn left and right, and js3 TWIST for rotation
     Winching: js1 button 10 for manual winching down and js1 button 11 for manual winching up and js2 button 10 is auto winch down and js2 button 11 is autowinch down.  Finally, js1 button 7 stops the winch.
     Feeding: js1 5 overrides that and dose reverse and js1 throttle turns the feeder on and off as well.
     Shooting: js2 button 5 turns it on and js2 trigger resets it
*/
/**
 *
 * @author Henry
 */
public class AllInOne implements JoystickLayout {

    public Joystick js1, js2, js3;

    public AllInOne(Joystick js1, Joystick js2, Joystick js3) {
        this.js1 = js1;
        this.js2 = js2;
        this.js3 = js3;
    }
    
    
    
    public boolean manualWinchDown() {
        return js1.getRawButton(10);
    }

    public boolean manualWinchUp() {
        return js1.getRawButton(11);
    }

    public boolean autoWinchDown() {
        return js2.getRawButton(10);
    }

    public boolean autoWinchUp() {
        return js2.getRawButton(11);
    }

    public boolean setFeederOn() {
        return js1.getThrottle() > .5;
    }

    public boolean overrideFeederReverse() {
        return js1.getRawButton(5);
    }

    public boolean electroMagnetOn() {
        return js2.getRawButton(5);
    }

    public boolean electroMagnetReset() {
        return js2.getTrigger() || js1.getTrigger();
    }

    
    //-js3.getY(), -js3.getX(), js3.getTwist()/ 2
    public double driveForward() {
        return -js3.getY();
    }

    public double driveLeft() {
        return -js3.getX();
    }

    public double driveRotation() {
        return js3.getTwist() / 2;
    }

    public boolean winchOff() {
        return js1.getRawButton(7);
    }
    
    
    
}
