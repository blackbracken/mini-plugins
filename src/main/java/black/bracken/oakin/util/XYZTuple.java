package black.bracken.oakin.util;

import org.bukkit.Location;

public final class XYZTuple {

    public final int x, y, z;

    public XYZTuple(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof XYZTuple)) return false;

        XYZTuple that = (XYZTuple) object;
        return this.x == that.x && this.y == that.y && this.z == that.z;
    }

    @Override
    public int hashCode() {
        return (x + z) * y;
    }

    public static XYZTuple fromLocation(Location location) {
        return new XYZTuple(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

}
