package org.contikios.cooja.plugins;

import org.contikios.cooja.Cooja;        // <-- use Cooja here
import org.contikios.cooja.Plugin;
import org.contikios.cooja.PluginType;
import org.contikios.cooja.SimEventCentral.LogOutputEvent;
import org.contikios.cooja.SimEventCentral.LogOutputListener;
import org.contikios.cooja.Simulation;
import org.jdom2.Element;

import javax.swing.*;
import java.util.Collection;
import java.util.Collections;

@PluginType(PluginType.PType.SIM_PLUGIN)
public class HeadlessLogListener implements Plugin {

  private final Simulation simulation;
  private final LogOutputListener listener;

  // *** IMPORTANT: Cooja, not GUI ***
  public HeadlessLogListener(Simulation simulation, Cooja gui) {
    this.simulation = simulation;

    listener = new LogOutputListener() {
      @Override
      public void newLogOutput(LogOutputEvent ev) {   // matches your COOJA version
        long ms = simulation.getSimulationTimeMillis();
        long h = ms / 3_600_000L, m = (ms / 60_000L) % 60, s = (ms / 1000L) % 60, u = ms % 1000L;
        String ts = h > 0 ? String.format("%d:%02d:%02d.%03d", h, m, s, u)
                          : String.format("%02d:%02d.%03d", m, s, u);
        System.out.println(ts + "\tID:" + ev.getMote().getID() + "\t" + ev.getMessage());
      }
    };

    simulation.getEventCentral().addLogOutputListener(listener);
  }

  @Override public void startPlugin() { }
  @Override public void closePlugin() { simulation.getEventCentral().removeLogOutputListener(listener); }
  @Override public JInternalFrame getCooja() { return null; }
  @Override public Collection<Element> getConfigXML() { return Collections.emptyList(); }
  @Override public boolean setConfigXML(Collection<Element> configXML, boolean visAvailable) { return true; }
}
