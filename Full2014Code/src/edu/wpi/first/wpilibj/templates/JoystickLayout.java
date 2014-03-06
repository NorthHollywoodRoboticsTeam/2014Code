/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;


/*
     Joystick spec for spit mechanum.
     Driving: js1 Y for forward and backward, js1 X for turn and js2 X for sideways
     Winching: js3 up and down on knob   and js3 button 4 is auto winch up and js3 button 3 is autowinch down.
     Feeding: Toggle js2 10 for on and off and js2 11 overrides that and dose reverse
     Shooting: js3 button 2 turns it on and js3 trigger resets it
*/

/**
 *
 * @author Henry
 */
public interface JoystickLayout {
    public boolean manualWinchDown();
    public boolean manualWinchUp();
    public boolean winchOff();
    public boolean autoWinchDown();
    public boolean autoWinchUp();
    public boolean setFeederOn();
    public boolean overrideFeederReverse();
    public boolean electroMagnetOn();
    public boolean electroMagnetReset();
    
    
    public double driveForward();
    public double driveLeft();
    public double driveRotation();
}
