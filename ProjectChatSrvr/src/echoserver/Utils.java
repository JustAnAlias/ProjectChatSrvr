/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package echoserver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author RL^
 */
public class Utils {
 public static Properties initProperties(String propertyFile) {
    Properties properties = new Properties();
    try (InputStream is = new FileInputStream(propertyFile)) {
      properties.load(is);
    } catch (IOException ex) {
      System.out.println(String.format("Could not locate the %1$s file.", propertyFile));
      return null;
    }
    return properties;
  }    
}
