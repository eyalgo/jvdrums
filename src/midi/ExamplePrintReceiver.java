package midi;

/*
 * ExamplePrintReceiver.java
 *
 * Created on 21. September 2000, 21:51
 */

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.SysexMessage;

class ExamplePrintReceiver implements Receiver {
    ExamplePrintReceiver() {
    }

    /*
     * public void setEnabled(boolean onoff) {this.onoff=onoff;}
     */
    public void send(MidiMessage event, long time) {
        /*
         * if (event instanceof ShortMessage) { // also.send(event,time); } // if (!onoff)
         * return;
         */
        //
        Integer i = new Integer(0);
        StringBuffer output = new StringBuffer();
        output.append("Received a MidiEvent: " + i.toHexString(event.getStatus())
                + " Length: " + event.getLength() + " at " + time + "\n");
        /*
         * if (event instanceof ShortMessage) { switch (event.getStatus()&0xf0) { case 0x90:
         * output.append(" Note On Key: "+((ShortMessage)event).getData1()+" Velocity:
         * "+((ShortMessage)event).getData2()); break; case 0x80: output.append(" Note Off Key:
         * "+((ShortMessage)event).getData1()+" Velocity: "+((ShortMessage)event).getData2());
         * break; case 0xb0: if (((ShortMessage)event).getData1()<120) output.append("
         * Controller No.: "+((ShortMessage)event).getData1()+" Value:
         * "+((ShortMessage)event).getData2()); else output.append(" ChannelMode Message No.:
         * "+((ShortMessage)event).getData1()+" Value: "+((ShortMessage)event).getData2());
         * break; case 0xe0: output.append(" Pitch lsb: "+((ShortMessage)event).getData1()+"
         * msb: "+((ShortMessage)event).getData2()); break; case 0xc0: output.append(" Program
         * Change No: "+((ShortMessage)event).getData1()+" Just for Test:
         * "+((ShortMessage)event).getData2()); break; case 0xd0: output.append(" Channel
         * Aftertouch Pressure: "+((ShortMessage)event).getData1()+" Just for Test:
         * "+((ShortMessage)event).getData2()); break; } } else
         */if (event instanceof SysexMessage) {
            output.append("   SysexMessage: " + (event.getStatus() - 256));
            byte[] data = ((SysexMessage) event).getData();
            for (int x = 0; x < data.length; x++)
                output.append(" " + i.toHexString(data[x]));
            System.out.println(output.toString());
        } else
            output.append("   MetaEvent");

        // System.out.println(output.toString());
    }

    public void close() {}

} // end of class
