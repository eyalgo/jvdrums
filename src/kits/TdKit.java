package kits;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public abstract class TdKit {
    private final int numberOfSubParts;
    private final int nameMaxLength;
    private final int starNameIndex;
    private final int maxNumberOfKits;
    protected TdSubPart[] subParts;

    protected TdKit(int numberOfSubParts, int nameMaxLength, int starNameIndex,
            int maxNumberOfKits) {
        this.numberOfSubParts = numberOfSubParts;
        this.nameMaxLength = nameMaxLength;
        this.starNameIndex = starNameIndex;
        this.maxNumberOfKits = maxNumberOfKits;
    }

    public TdKit(int numberOfSubParts, int nameMaxLength, int starNameIndex,
            int maxNumberOfKits, TdSubPart[] subParts) {
        this(numberOfSubParts, nameMaxLength, starNameIndex, maxNumberOfKits);
        this.subParts = subParts;
    }

    public final TdKit setNewId(Integer newId) throws InvalidMidiDataException {
        if (newId < 1 || newId > maxNumberOfKits) {
            throw new IllegalArgumentException(" id " + newId);
        }
        
        final TdSubPart[] newSubParts = new TdSubPart[numberOfSubParts];
        for (int i = 0; i < newSubParts.length; i++) {
            newSubParts[i] = getNewSubPart(subParts[i], newId);
        }
        final TdKit newKit = getNewKit(newSubParts);
        return newKit;
    }

    public final SysexMessage getMessage() throws InvalidMidiDataException {
        byte[] data = null;
        for (int i = 0; i < this.numberOfSubParts; i++) {
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
        final byte[] rawName = new byte[nameMaxLength];
        for (int i = 0; i < nameMaxLength; i++) {
            rawName[i] = partOneData[i + starNameIndex];
        }
        String temp = new String(rawName);
        final char nothing = 0x00;
        temp = StringUtils.remove(temp, nothing);
        final String name = StringUtils.trimToEmpty(temp);
        return name;
    }

    public abstract int getId();

    protected abstract TdSubPart getNewSubPart(TdSubPart part, Integer newId)
            throws InvalidMidiDataException;

    protected abstract TdKit getNewKit(TdSubPart[] newSubParts);
}