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


public class Mechanum extends SplitMechanum {

    public Mechanum(Joystick js1, Joystick js2, Joystick js3) {
        super(js1, js2, js3);
    }

    
    //-js2.getY(), -js2.getX(), js1.getX() / 2, 0
    public double driveForward() {
        if (js2.getThrottle() > .5) {
            System.out.println("drive style one");
            return -js2.getY();
        } else {
            return js2.getY();
        }
    }

    public double driveLeft() {
        if (js2.getThrottle() > .5) {
            return -js2.getX();
        } else {
            return js2.getX();
        }
    }

    public double driveRotation() {
        return js1.getX() / 2;
    }

    
    
    
    
    
    
    
}
