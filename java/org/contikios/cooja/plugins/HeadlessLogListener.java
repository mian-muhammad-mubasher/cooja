package org.contikios.cooja.plugins;

import org.contikios.cooja.*;
import org.contikios.cooja.SimEventCentral.LogOutputEvent;
import org.contikios.cooja.SimEventCentral.LogOutputListener;
import org.jdom2.Element;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@PluginType(PluginType.PType.SIM_CONTROL_PLUGIN)
public class HeadlessLogListener implements Plugin {

    private final Simulation simulation;
    private final LogOutputListener logListener;

    public HeadlessLogListener(Simulation simulation, Cooja gui) {
        this.simulation = simulation;

        // Define how we handle log output
        logListener = new LogOutputListener() {
            @Override
            public void newLogOutput(LogOutputEvent ev) {
                String time = formatTimestamp(simulation.getSimulationTimeMillis());
                String message = ev.getMessage();
                int moteID = ev.getMote().getID();
                System.out.println(time + " [Mote " + moteID + "]: " + message);
            }
        };

        // Register the listener with COOJA
        simulation.getEventCentral().addLogOutputListener(logListener);
    }

    @Override
    public void startPlugin() {
        // No startup behavior needed
    }

    @Override
    public void closePlugin() {
        // Unregister the listener on plugin close
        simulation.getEventCentral().removeLogOutputListener(logListener);
    }

    @Override
    public JInternalFrame getCooja() {
        return null; // No GUI
    }

    @Override
    public Collection<Element> getConfigXML() {
        return Collections.emptyList(); // No config
    }

    @Override
    public boolean setConfigXML(Collection<Element> configXML, boolean visAvailable) {
        return true; // No config needed
    }

    private String formatTimestamp(long simMillis) {
        return "[" + new SimpleDateFormat("HH:mm:ss.SSS").format(new Date(simMillis)) + "]";
    }
}
