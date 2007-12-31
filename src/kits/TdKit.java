package kits;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

import kits.info.TdInfo;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import exceptions.BadChecksumException;
import exceptions.BadMessageLengthException;
import exceptions.NotRolandException;

public abstract class TdKit {
    protected final TdInfo tdInfo; 
    protected TdSubPart[] subParts;

    protected TdKit(TdInfo tdInfo, byte[] rawData) throws BadMessageLengthException, BadChecksumException, NotRolandException, InvalidMidiDataException {
        this.tdInfo = tdInfo;
        if (rawData.length != tdInfo.getKitSize()) {
            throw new BadMessageLengthException(rawData.length);
        }
        subParts = new TdSubPart[tdInfo.getNumberOfSubParts()];
        
        final List<Integer> indexes = new ArrayList<Integer>();
        for (int i = 0; i < rawData.length; i++) {
            if ((rawData[i] & 0xFF) == 240) {
                indexes.add(i);
            }
        }
        try {
            for (int i = 0; i < subParts.length; i++) {
                int from = indexes.get(i);
                int to;
                if (i == (subParts.length - 1)) {
                    to = rawData.length;
                } else {
                   to =  indexes.get(i+1);
                }
                final byte[] partRawData = ArrayUtils.subarray(rawData, from, to);
                subParts[i] = new TdSubPart(partRawData, tdInfo);
            }
            
        }
        catch (RuntimeException e) {
            throw new InvalidMidiDataException();
        }
    }

    public TdKit(TdInfo tdInfo, TdSubPart[] subParts) {
        this.tdInfo = tdInfo;
        this.subParts = subParts;
    }

    public final TdKit setNewId(Integer newId) throws InvalidMidiDataException {
        if (newId < 1 || newId > tdInfo.getMaxNumberOfKits()) {
            throw new IllegalArgumentException(" id " + newId);
        }
        
        final TdSubPart[] newSubParts = new TdSubPart[tdInfo.getNumberOfSubParts()];
        for (int i = 0; i < newSubParts.length; i++) {
            newSubParts[i] = tdInfo.getNewSubPart(subParts[i], newId);
        }
        final TdKit newKit = getNewKit(newSubParts);
        return newKit;
    }

    public final SysexMessage getMessage() throws InvalidMidiDataException {
        byte[] data = null;
        for (int i = 0; i < tdInfo.getNumberOfSubParts(); i++) {
            data = ArrayUtils.addAll(data, subParts[i].getMessage());
        }
        final SysexMessage result = new VdrumsSysexMessage();

        result.setMessage(data, data.length);
        return result;
    }

    protected final int setImutableId() {
        int result = 0;
        for (int i = 0; i < subParts.length; i++) {
            result += this.subParts[i].getId();
        }
        if (result % subParts.length != 0) {
            throw new RuntimeException();
        }
        result = result / subParts.length;

        return result;
    }

    @Override
    public final int hashCode() {
        int result = 17;
        for (int i = 0; i < subParts.length; i++) {
            result = 23 * result + subParts[i].hashCode();
        }
        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof TdKit) {
            final TdKit other = (TdKit) obj;
            try {
                return this.getMessage().equals(other.getMessage());
            }
            catch (InvalidMidiDataException e) {
                throw new RuntimeException("Got InvalidMidiDataException in equals "
                        + this.toString());
            }
        } else {
            return false;
        }
    }

    @Override
    public final String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        result.append(newLine);
        result.append(this.getClass().getSimpleName()).append(" {");
        result.append(newLine);
        result.append(" Name: ").append(this.getName()).append(newLine);
        result.append(" id: ").append(this.getId()).append(newLine);
        result.append("}");
        return result.toString();
    }

    public final VdrumsSysexMessage[] getKitSubParts() {
        return subParts;
    }

    public final String getName() {
        final byte[] partOneData = subParts[0].getMessage();
        final byte[] rawName = new byte[tdInfo.getMaxLengthName()];
        for (int i = 0; i < tdInfo.getMaxLengthName(); i++) {
            rawName[i] = partOneData[i + tdInfo.getStartNameIndex()];
        }
        String temp = new String(rawName);
        final char nothing = 0x00;
        temp = StringUtils.remove(temp, nothing);
        final String name = StringUtils.trimToEmpty(temp);
        return name;
    }

    public abstract int getId();

//    protected abstract TdSubPart getNewSubPart(TdSubPart part, Integer newId)
//            throws InvalidMidiDataException;

    protected final TdKit getNewKit(TdSubPart[] newSubParts) {
        return tdInfo.getNewKit(newSubParts);
    }

    public final String getTdInfoName() {
        return tdInfo.getNameToDisplay();
    }
}