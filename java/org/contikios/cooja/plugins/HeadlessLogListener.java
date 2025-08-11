package org.contikios.cooja.plugins;

import org.contikios.cooja.GUI;
import org.contikios.cooja.Plugin;
import org.contikios.cooja.PluginType;
import org.contikios.cooja.SimEventCentral.LogOutputEvent;
import org.contikios.cooja.SimEventCentral.LogOutputListener;
import org.contikios.cooja.Simulation;
import org.jdom2.Element;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@PluginType(PluginType.PType.SIM_PLUGIN)
public class HeadlessLogListener implements Plugin {

  private final Simulation simulation;
  private final LogOutputListener listener;

  public HeadlessLogListener(Simulation simulation, GUI gui) {
    this.simulation = simulation;

    // IMPORTANT: use the correct method name for your COOJA version: newLogOutput(...)
    listener = new LogOutputListener() {
      @Override
      public void newLogOutput(LogOutputEvent ev) {
        // Match LogListener’s “formatted time” style.
        String ts = formatSimTime(simulation.getSimulationTimeMillis());
        int id = ev.getMote().getID();
        String msg = ev.getMessage();
        // Print to stdout (no files, as requested)
        System.out.println(ts + "\tID:" + id + "\t" + msg);
      }
    };

    // Subscribe to mote logs
    simulation.getEventCentral().addLogOutputListener(listener);
  }

  @Override
  public void startPlugin() {
    // nothing
  }

  @Override
  public void closePlugin() {
    simulation.getEventCentral().removeLogOutputListener(listener);
  }

  @Override
  public JInternalFrame getCooja() {
    return null; // no GUI
  }

  @Override
  public Collection<Element> getConfigXML() {
    return Collections.emptyList(); // no config
  }

  @Override
  public boolean setConfigXML(Collection<Element> configXML, boolean visAvailable) {
    return true; // no config
  }

  private static String formatSimTime(long ms) {
    long h = ms / 3_600_000L;
    long m = (ms / 60_000L) % 60;
    long s = (ms / 1000L) % 60;
    long milli = ms % 1000L;
    if (h > 0) return String.format("%d:%02d:%02d.%03d", h, m, s, milli);
    return String.format("%02d:%02d.%03d", m, s, milli);
  }
}
