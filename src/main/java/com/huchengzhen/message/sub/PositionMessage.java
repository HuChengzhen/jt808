package com.huchengzhen.message.sub;

import com.huchengzhen.EWSN;
import com.huchengzhen.util.BCD;
import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class PositionMessage {
    byte[] status;
    long latitude;
    long longitude;
    int altitude;
    int speed;
    int direction;
    byte[] time;

    public boolean isAcc() {
        return (status[3] & 0b00000001) != 0;
    }

    public boolean isPosition() {
        return (status[3] & 0b00000010) != 0;
    }

    public EWSN[] ewsn() {
        EWSN[] ewsns = new EWSN[2];
        if ((status[3] & 0b00000100) == 0) {
            ewsns[0] = EWSN.N;
        } else {
            ewsns[0] = EWSN.S;
        }

        if ((status[3] & 0b00001000) == 0) {
            ewsns[1] = EWSN.E;
        } else {
            ewsns[1] = EWSN.W;
        }

        return ewsns;
    }

    public String getTimeString() {
        return BCD.BCDtoString(time);
    }

    static public PositionMessage fromByteBuf(ByteBuf buf) {
        PositionMessage msg = new PositionMessage();
        buf.readBytes(4);

        msg.status = new byte[4];
        buf.readBytes(msg.status);
        msg.latitude = buf.readUnsignedInt();
        msg.longitude = buf.readUnsignedInt();
        msg.altitude = buf.readUnsignedShort();
        msg.speed = buf.readUnsignedShort();
        msg.direction = buf.readUnsignedShort();
        msg.time = new byte[6];
        buf.readBytes(msg.time);
        return msg;
    }
}
