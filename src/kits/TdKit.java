package kits;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

public interface TdKit {
    public abstract int getId();
    public abstract SysexMessage getMessage() throws InvalidMidiDataException;
    public abstract String getName();
    public abstract TdKit setNewId(Integer newId) throws InvalidMidiDataException;

}