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
public class SplitAllInOne extends AllInOne {

    public SplitAllInOne(Joystick js1,Joystick js2, Joystick js3) {
        super(js1, js2, js3);
    }

    public double driveLeft() {
        if (js3.getRawAxis(4) > .5) {
            System.out.println("Driving mode one!!!");
            return -js3.getTwist();
        } else {
            System.out.println("dirving mode two");
            return js3.getTwist();
        }
    }

    public double driveRotation() {
        return js3.getX() / 2;
    }
    
    

    
    
    
    
}
