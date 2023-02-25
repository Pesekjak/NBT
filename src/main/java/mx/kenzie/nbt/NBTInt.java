package mx.kenzie.nbt;

import mx.kenzie.nbt.visitor.NBTStringVisitor;
import mx.kenzie.nbt.visitor.NBTVisitor;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public record NBTInt(Integer value) implements NBTValue<Integer>, NBT {

    public NBTInt(Object value) {
        this(((Number) value).intValue());
    }

    public NBTInt(InputStream stream) throws IOException {
        this(decodeInt(stream));
    }

    static int decodeInt(InputStream stream) throws IOException {
        final int a = stream.read(), b = stream.read(), c = stream.read(), d = stream.read();
        if (d < 0) throw new EOFException();
        return ((a << 24) + (b << 16) + (c << 8) + d);
    }

    static void encodeInt(OutputStream stream, int value) throws IOException {
        final byte[] buffer = new byte[4];
        buffer[0] = (byte) (value >>> 24);
        buffer[1] = (byte) (value >>> 16);
        buffer[2] = (byte) (value >>> 8);
        buffer[3] = (byte) (value);
        stream.write(buffer);
    }

    @Override
    public String toString() {
        return new NBTStringVisitor().visitNBT(this);
    }

    @Override
    public void write(OutputStream stream) throws IOException {
        NBTInt.encodeInt(stream, value);
    }

    @Override
    public Tag tag() {
        return Tag.INT;
    }

    @Override
    public void accept(NBTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        NBTInt nbtInt = (NBTInt) o;

        return value.equals(nbtInt.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public NBTInt clone() {
        try {
            return (NBTInt) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}
