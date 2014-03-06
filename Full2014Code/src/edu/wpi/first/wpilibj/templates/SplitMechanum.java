/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author Henry
 */
/*
The drive is split so that turning and forward/backward are on JS1 and sideways is JS2.  (Arcade drive plus sideways)
*/

/*
     Joystick spec for spit mechanum.
     Driving: js1 Y for forward and backward, js1 X for turn and js2 X for sideways
     Winching: js3 up and down on knob   and js3 button 4 is auto winch up and js3 button 3 is autowinch down.
     Feeding: Toggle js2 10 for on and off and js3 11 overrides that and dose reverse and js3 throttle turns the feeder on and off as well.
     Shooting: js3 button 2 turns it on and js3 trigger resets it
*/

public class SplitMechanum implements JoystickLayout {

    Joystick js1, js2, js3;

    public SplitMechanum(Joystick js1, Joystick js2, Joystick js3) {
        this.js1 = js1;
        this.js2 = js2;
        this.js3 = js3;
    }
    
    
    public boolean manualWinchDown() {
        return js3.getRawAxis(6) == -1;
    }
    
    

    public boolean manualWinchUp() {
        return js3.getRawAxis(6) == 1;
    
    }

    public boolean autoWinchDown() {
        return js3.getRawButton(3);
    }

    public boolean autoWinchUp() {
        return js3.getRawButton(4);
    }

    public boolean setFeederOn() {
        return js3.getRawAxis(4) < .5;
    }


    public boolean overrideFeederReverse() {
        return js3.getRawButton(11);
    }

    public boolean electroMagnetOn() {
        return js3.getRawButton(2);
        
    }

    public boolean electroMagnetReset() {
        return js3.getTrigger();
       
    }

    public double driveForward() {
        return js1.getY();
    }

    public double driveLeft() {
        return js2.getY();
    }

    public double driveRotation() {
        return js1.getX();
    }
    
}
