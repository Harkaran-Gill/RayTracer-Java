package com.ray;

public class Point3 extends Vec3{
    public Point3() {super();}
    public Point3(double x, double y, double z) {super(x, y, z);}
    public Point3 (Vec3 vec) {super(vec.x(), vec.y(), vec.z());}

    public Point3 addSelf(Vec3 other){
        return (Point3) super.addSelf(other);
    }

    public Point3 subSelf(Vec3 other){ return (Point3) super.subSelf(other);}

    @Override
    public Point3 add(Vec3 other) {
        Vec3 result = super.add(other);
        return new Point3(result);
    }

    @Override
    public Point3 sub(Vec3 other) {
        Vec3 result = super.sub(other);
        return new Point3(result);
    }


    public static Point3 randomInUnitDisk(){
        while(true){
            Point3 p = new  Point3(Utility.randomDouble(-1,1), Utility.randomDouble(-1,1), 0);
            if (p.magnitudeSquared() < 1){
                return p;
            }
        }
    }
}
