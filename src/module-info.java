/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

module Jeditor {
    requires GoDrive;
    requires flowless;
    requires com.sun.jna;
    requires reactfx;
    requires richtextfx;
    requires undofx;
    requires wellbehavedfx;
    requires javafx.swt;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.web;
    exports Jeditor;
   opens Jeditor to javafx.fxml;
}
