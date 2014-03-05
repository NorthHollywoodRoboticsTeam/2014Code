/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.SpeedController;

/**
 *
 * @author Henry
 */
public class InvertedSpeedController implements  SpeedController {

    SpeedController sc;
    
    public InvertedSpeedController(SpeedController sc) {
        this.sc = sc;
    }
    
    
    public double get() {
    return -sc.get();
    }

    public void set(double speed, byte syncGroup) {
    sc.set(-speed, syncGroup);
    }

    public void set(double speed) {
        sc.set(-speed);
    }

    public void disable() {
        sc.disable();
    }

    public void pidWrite(double output) {
        sc.pidWrite(output);
    }
    
}
