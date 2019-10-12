package cgtools;

import static cgtools.Vec3.*;

/**
 * A simple 4x4 matrix class using double values. Matrices are non-mutable and
 * can be passed around as values. The matrix is stored in one double array in
 * column-major format.
 *
 * @author henrik
 */
public final class Mat4 {
    /** <code>identity</code> The identity matrix. */
    public static final Mat4 identity = new Mat4().makeIdentity();

    private double[] values;

    private Mat4() {
        makeIdentity();
    }

    public Mat4(double m00, double m01, double m02, double m03, double m10, double m11, double m12, double m13,
            double m20, double m21, double m22, double m23, double m30, double m31, double m32, double m33) {
        makeIdentity();
        set(0, 0, m00);
        set(0, 1, m01);
        set(0, 2, m02);
        set(0, 3, m03);
        set(1, 0, m10);
        set(1, 1, m11);
        set(1, 2, m12);
        set(1, 3, m13);
        set(2, 0, m20);
        set(2, 1, m21);
        set(2, 2, m22);
        set(2, 3, m23);
        set(3, 0, m30);
        set(3, 1, m31);
        set(3, 2, m32);
        set(3, 3, m33);
    }

    /**
     * Construct a new matrix from three base vectors.
     *
     * @param b0 The first basis Vec3.
     * @param b1 The second basis Vec3.
     * @param b2 The third basis Vec3.
     */
    public Mat4(Vec3 b0, Vec3 b1, Vec3 b2) {
        makeIdentity();
        set(0, 0, b0.x);
        set(1, 0, b0.y);
        set(2, 0, b0.z);
        set(0, 1, b1.x);
        set(1, 1, b1.y);
        set(2, 1, b1.z);
        set(0, 2, b2.x);
        set(1, 2, b2.y);
        set(2, 2, b2.z);
    }

    /**
     * Construct a matrix from an array of double values.
     *
     * @param m An array of 16 double values.
     */
    public Mat4(double[] m) {
        values = m.clone();
    }

    /**
     * Construct a new matrix that represents a translation.
     *
     * @param t The translation Vec3.
     * @return The translation matrix.
     */
    public static Mat4 translate(Vec3 t) {
        Mat4 m = new Mat4();
        m.set(3, 0, t.x);
        m.set(3, 1, t.y);
        m.set(3, 2, t.z);
        return m;
    }

    public static Mat4 translate(double x, double y, double z) {
        Mat4 m = new Mat4();
        m.set(3, 0, x);
        m.set(3, 1, y);
        m.set(3, 2, z);
        return m;
    }

    /**
     * Construct a new matrix that represents a rotation.
     *
     * @param axis  The rotation axis.
     * @param angle The angle of rotaion in degree.
     * @return The rotation matrix.
     */
    public static Mat4 rotate(Vec3 axis, double angle) {
        final Mat4 m = new Mat4();
        final double rad = (angle / 180.0f) * ((double) Math.PI);
        final double cosa = (double) Math.cos(rad);
        final double sina = (double) Math.sin(rad);
        final double l = Math.sqrt(axis.x * axis.x + axis.y * axis.y + axis.z * axis.z);
        final double rx = axis.x / l;
        final double ry = axis.y / l;
        final double rz = axis.z / l;
        final double icosa = 1 - cosa;

        m.set(0, 0, (double) (icosa * rx * rx + cosa));
        m.set(0, 1, (double) (icosa * rx * ry + rz * sina));
        m.set(0, 2, (double) (icosa * rx * rz - ry * sina));

        m.set(1, 0, (double) (icosa * rx * ry - rz * sina));
        m.set(1, 1, (double) (icosa * ry * ry + cosa));
        m.set(1, 2, (double) (icosa * ry * rz + rx * sina));

        m.set(2, 0, (double) (icosa * rx * rz + ry * sina));
        m.set(2, 1, (double) (icosa * ry * rz - rx * sina));
        m.set(2, 2, (double) (icosa * rz * rz + cosa));
        return m;
    }

    public static Mat4 rotate(double ax, double ay, double az, double angle) {
        return rotate(vec3(ax, ay, az), angle);
    }

    /**
     * Construct a new matrix that represents a scale transformation.
     *
     * @param s The three scale factors.
     * @return The scale matrix.
     */
    public static Mat4 scale(Vec3 s) {
        Mat4 m = new Mat4();
        m.set(0, 0, s.x);
        m.set(1, 1, s.y);
        m.set(2, 2, s.z);
        return m;
    }

    public static Mat4 scale(double x, double y, double z) {
        Mat4 m = new Mat4();
        m.set(0, 0, x);
        m.set(1, 1, y);
        m.set(2, 2, z);
        return m;
    }

    /**
     * Get one value of one element from the matrix.
     *
     * @param c The column from which to get the value.
     * @param r The row from which to get the value.
     * @return The value at position (c, r).
     */
    public double get(int c, int r) {
        return values[4 * c + r];
    }

    /**
     * Set the value of one matrix element.
     *
     * @param c The column in which to set the value.
     * @param r The row in which to set the value.
     * @param v The new value for the matrix element at position (c, r)
     */
    private void set(int c, int r, double v) {
        values[4 * c + r] = v;
    }

    /**
     * Calculate the product of two matrices.
     *
     * @param m The second matrix.
     * @return The product.
     */
    public Mat4 multiply(Mat4 m) {
        // Optimzed version.
        Mat4 n = new Mat4();
        {
            {
                double v = 0;
                v += values[4 * 0 + 0] * m.values[4 * 0 + 0];
                v += values[4 * 1 + 0] * m.values[4 * 0 + 1];
                v += values[4 * 2 + 0] * m.values[4 * 0 + 2];
                v += values[4 * 3 + 0] * m.values[4 * 0 + 3];
                n.values[4 * 0 + 0] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 1] * m.values[4 * 0 + 0];
                v += values[4 * 1 + 1] * m.values[4 * 0 + 1];
                v += values[4 * 2 + 1] * m.values[4 * 0 + 2];
                v += values[4 * 3 + 1] * m.values[4 * 0 + 3];
                n.values[4 * 0 + 1] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 2] * m.values[4 * 0 + 0];
                v += values[4 * 1 + 2] * m.values[4 * 0 + 1];
                v += values[4 * 2 + 2] * m.values[4 * 0 + 2];
                v += values[4 * 3 + 2] * m.values[4 * 0 + 3];
                n.values[4 * 0 + 2] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 3] * m.values[4 * 0 + 0];
                v += values[4 * 1 + 3] * m.values[4 * 0 + 1];
                v += values[4 * 2 + 3] * m.values[4 * 0 + 2];
                v += values[4 * 3 + 3] * m.values[4 * 0 + 3];
                n.values[4 * 0 + 3] = v;
            }
        }
        {
            {
                double v = 0;
                v += values[4 * 0 + 0] * m.values[4 * 1 + 0];
                v += values[4 * 1 + 0] * m.values[4 * 1 + 1];
                v += values[4 * 2 + 0] * m.values[4 * 1 + 2];
                v += values[4 * 3 + 0] * m.values[4 * 1 + 3];
                n.values[4 * 1 + 0] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 1] * m.values[4 * 1 + 0];
                v += values[4 * 1 + 1] * m.values[4 * 1 + 1];
                v += values[4 * 2 + 1] * m.values[4 * 1 + 2];
                v += values[4 * 3 + 1] * m.values[4 * 1 + 3];
                n.values[4 * 1 + 1] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 2] * m.values[4 * 1 + 0];
                v += values[4 * 1 + 2] * m.values[4 * 1 + 1];
                v += values[4 * 2 + 2] * m.values[4 * 1 + 2];
                v += values[4 * 3 + 2] * m.values[4 * 1 + 3];
                n.values[4 * 1 + 2] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 3] * m.values[4 * 1 + 0];
                v += values[4 * 1 + 3] * m.values[4 * 1 + 1];
                v += values[4 * 2 + 3] * m.values[4 * 1 + 2];
                v += values[4 * 3 + 3] * m.values[4 * 1 + 3];
                n.values[4 * 1 + 3] = v;
            }
        }
        {
            {
                double v = 0;
                v += values[4 * 0 + 0] * m.values[4 * 2 + 0];
                v += values[4 * 1 + 0] * m.values[4 * 2 + 1];
                v += values[4 * 2 + 0] * m.values[4 * 2 + 2];
                v += values[4 * 3 + 0] * m.values[4 * 2 + 3];
                n.values[4 * 2 + 0] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 1] * m.values[4 * 2 + 0];
                v += values[4 * 1 + 1] * m.values[4 * 2 + 1];
                v += values[4 * 2 + 1] * m.values[4 * 2 + 2];
                v += values[4 * 3 + 1] * m.values[4 * 2 + 3];
                n.values[4 * 2 + 1] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 2] * m.values[4 * 2 + 0];
                v += values[4 * 1 + 2] * m.values[4 * 2 + 1];
                v += values[4 * 2 + 2] * m.values[4 * 2 + 2];
                v += values[4 * 3 + 2] * m.values[4 * 2 + 3];
                n.values[4 * 2 + 2] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 3] * m.values[4 * 2 + 0];
                v += values[4 * 1 + 3] * m.values[4 * 2 + 1];
                v += values[4 * 2 + 3] * m.values[4 * 2 + 2];
                v += values[4 * 3 + 3] * m.values[4 * 2 + 3];
                n.values[4 * 2 + 3] = v;
            }
        }
        {
            {
                double v = 0;
                v += values[4 * 0 + 0] * m.values[4 * 3 + 0];
                v += values[4 * 1 + 0] * m.values[4 * 3 + 1];
                v += values[4 * 2 + 0] * m.values[4 * 3 + 2];
                v += values[4 * 3 + 0] * m.values[4 * 3 + 3];
                n.values[4 * 3 + 0] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 1] * m.values[4 * 3 + 0];
                v += values[4 * 1 + 1] * m.values[4 * 3 + 1];
                v += values[4 * 2 + 1] * m.values[4 * 3 + 2];
                v += values[4 * 3 + 1] * m.values[4 * 3 + 3];
                n.values[4 * 3 + 1] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 2] * m.values[4 * 3 + 0];
                v += values[4 * 1 + 2] * m.values[4 * 3 + 1];
                v += values[4 * 2 + 2] * m.values[4 * 3 + 2];
                v += values[4 * 3 + 2] * m.values[4 * 3 + 3];
                n.values[4 * 3 + 2] = v;
            }
            {
                double v = 0;
                v += values[4 * 0 + 3] * m.values[4 * 3 + 0];
                v += values[4 * 1 + 3] * m.values[4 * 3 + 1];
                v += values[4 * 2 + 3] * m.values[4 * 3 + 2];
                v += values[4 * 3 + 3] * m.values[4 * 3 + 3];
                n.values[4 * 3 + 3] = v;
            }
        }
        return n;
    }

    protected Mat4 multSlow(Mat4 m) {
        Mat4 n = new Mat4();
        for (int c = 0; c != 4; c++) {
            for (int r = 0; r != 4; r++) {
                double v = 0;
                for (int k = 0; k != 4; k++)
                    v += get(k, r) * m.get(c, k);
                n.set(c, r, v);
            }
        }
        return n;
    }

    /**
     * Transform a point by the current matrix. The homogenous coordinate is assumed
     * to be 1.0.
     *
     * @param v The point.
     * @return The transformed point.
     */
    public Vec3 transformPoint(Vec3 v) {
        final double x = get(0, 0) * v.x + get(1, 0) * v.y + get(2, 0) * v.z + get(3, 0);
        final double y = get(0, 1) * v.x + get(1, 1) * v.y + get(2, 1) * v.z + get(3, 1);
        final double z = get(0, 2) * v.x + get(1, 2) * v.y + get(2, 2) * v.z + get(3, 2);
        return vec3(x, y, z);
    }

    /**
     * Transform a direction by the current matrix. The homogenous coordinate is
     * assumed to be 0.0.
     *
     * @param v The direction Vec3.
     * @return The transformed point.
     */
    public Vec3 transformDirection(Vec3 v) {
        double x = get(0, 0) * v.x + get(1, 0) * v.y + get(2, 0) * v.z;
        double y = get(0, 1) * v.x + get(1, 1) * v.y + get(2, 1) * v.z;
        double z = get(0, 2) * v.x + get(1, 2) * v.y + get(2, 2) * v.z;
        return vec3(x, y, z);
    }

    /**
     * Transform a direction by the inverse of the current matrix. The homogenous
     * coordinate is assumed to be 0.0.
     *
     * @param v The direction Vec3.
     * @return The transformed direction.
     */
    public Vec3 transformInverseDirection(Vec3 v) {
        double x = get(0, 0) * v.x + get(0, 1) * v.y + get(0, 2) * v.z;
        double y = get(1, 0) * v.x + get(1, 1) * v.y + get(1, 2) * v.z;
        double z = get(2, 0) * v.x + get(2, 1) * v.y + get(2, 2) * v.z;
        return vec3(x, y, z);
    }

    public Mat4 transpose() {
        Mat4 n = new Mat4();
        for (int c = 0; c != 4; c++) {
            for (int r = 0; r != 4; r++) {
                n.set(c, r, get(r, c));
            }
        }
        return n;
    }

    /**
     * Calculate the inverse matrix. The matrix is assumed to be orthonormal.
     *
     * @return The inverse matrix.
     */
    protected Mat4 invertRigid() {
        /*
         * this only works for rigid body transformations!
         */
        /*
         * calculate the inverse rotation by transposing the upper 3x3 submatrix.
         */
        Mat4 ri = new Mat4();
        for (int c = 0; c != 3; c++)
            for (int r = 0; r != 3; r++)
                ri.set(c, r, get(r, c));
        /*
         * calculate the inverse translation
         */
        Mat4 ti = new Mat4();
        ti.set(3, 0, -get(3, 0));
        ti.set(3, 1, -get(3, 1));
        ti.set(3, 2, -get(3, 2));
        return ri.multiply(ti);
    }

    private Mat4 makeIdentity() {
        values = new double[16];
        set(0, 0, 1.0f);
        set(1, 1, 1.0f);
        set(2, 2, 1.0f);
        set(3, 3, 1.0f);
        return this;
    }

    /**
     * Calculate the full inverse of this matrix. This takes some time.
     *
     * @return The inverse matrix.
     */
    public Mat4 invertFull() {
        Mat4 ret = new Mat4();
        double[] mat = values;
        double[] dst = ret.values;
        double[] tmp = new double[12];

        /* temparray for pairs */
        double src[] = new double[16];

        /* array of transpose source matrix */
        double det;

        /* determinant */
        /*
         * transpose matrix
         */
        for (int i = 0; i < 4; i++) {
            src[i] = mat[i * 4];
            src[i + 4] = mat[i * 4 + 1];
            src[i + 8] = mat[i * 4 + 2];
            src[i + 12] = mat[i * 4 + 3];
        }

        /* calculate pairs for first 8 elements (cofactors) */
        tmp[0] = src[10] * src[15];
        tmp[1] = src[11] * src[14];
        tmp[2] = src[9] * src[15];
        tmp[3] = src[11] * src[13];
        tmp[4] = src[9] * src[14];
        tmp[5] = src[10] * src[13];
        tmp[6] = src[8] * src[15];
        tmp[7] = src[11] * src[12];
        tmp[8] = src[8] * src[14];
        tmp[9] = src[10] * src[12];
        tmp[10] = src[8] * src[13];
        tmp[11] = src[9] * src[12];

        /* calculate first 8 elements (cofactors) */
        dst[0] = tmp[0] * src[5] + tmp[3] * src[6] + tmp[4] * src[7];
        dst[0] -= tmp[1] * src[5] + tmp[2] * src[6] + tmp[5] * src[7];
        dst[1] = tmp[1] * src[4] + tmp[6] * src[6] + tmp[9] * src[7];
        dst[1] -= tmp[0] * src[4] + tmp[7] * src[6] + tmp[8] * src[7];
        dst[2] = tmp[2] * src[4] + tmp[7] * src[5] + tmp[10] * src[7];
        dst[2] -= tmp[3] * src[4] + tmp[6] * src[5] + tmp[11] * src[7];
        dst[3] = tmp[5] * src[4] + tmp[8] * src[5] + tmp[11] * src[6];
        dst[3] -= tmp[4] * src[4] + tmp[9] * src[5] + tmp[10] * src[6];
        dst[4] = tmp[1] * src[1] + tmp[2] * src[2] + tmp[5] * src[3];
        dst[4] -= tmp[0] * src[1] + tmp[3] * src[2] + tmp[4] * src[3];
        dst[5] = tmp[0] * src[0] + tmp[7] * src[2] + tmp[8] * src[3];
        dst[5] -= tmp[1] * src[0] + tmp[6] * src[2] + tmp[9] * src[3];
        dst[6] = tmp[3] * src[0] + tmp[6] * src[1] + tmp[11] * src[3];
        dst[6] -= tmp[2] * src[0] + tmp[7] * src[1] + tmp[10] * src[3];
        dst[7] = tmp[4] * src[0] + tmp[9] * src[1] + tmp[10] * src[2];
        dst[7] -= tmp[5] * src[0] + tmp[8] * src[1] + tmp[11] * src[2];

        /* calculate pairs for second 8 elements (cofactors) */
        tmp[0] = src[2] * src[7];
        tmp[1] = src[3] * src[6];
        tmp[2] = src[1] * src[7];
        tmp[3] = src[3] * src[5];
        tmp[4] = src[1] * src[6];
        tmp[5] = src[2] * src[5];
        tmp[6] = src[0] * src[7];
        tmp[7] = src[3] * src[4];
        tmp[8] = src[0] * src[6];
        tmp[9] = src[2] * src[4];
        tmp[10] = src[0] * src[5];
        tmp[11] = src[1] * src[4];

        /* calculate second 8 elements (cofactors) */
        dst[8] = tmp[0] * src[13] + tmp[3] * src[14] + tmp[4] * src[15];
        dst[8] -= tmp[1] * src[13] + tmp[2] * src[14] + tmp[5] * src[15];
        dst[9] = tmp[1] * src[12] + tmp[6] * src[14] + tmp[9] * src[15];
        dst[9] -= tmp[0] * src[12] + tmp[7] * src[14] + tmp[8] * src[15];
        dst[10] = tmp[2] * src[12] + tmp[7] * src[13] + tmp[10] * src[15];
        dst[10] -= tmp[3] * src[12] + tmp[6] * src[13] + tmp[11] * src[15];
        dst[11] = tmp[5] * src[12] + tmp[8] * src[13] + tmp[11] * src[14];
        dst[11] -= tmp[4] * src[12] + tmp[9] * src[13] + tmp[10] * src[14];
        dst[12] = tmp[2] * src[10] + tmp[5] * src[11] + tmp[1] * src[9];
        dst[12] -= tmp[4] * src[11] + tmp[0] * src[9] + tmp[3] * src[10];
        dst[13] = tmp[8] * src[11] + tmp[0] * src[8] + tmp[7] * src[10];
        dst[13] -= tmp[6] * src[10] + tmp[9] * src[11] + tmp[1] * src[8];
        dst[14] = tmp[6] * src[9] + tmp[11] * src[11] + tmp[3] * src[8];
        dst[14] -= tmp[10] * src[11] + tmp[2] * src[8] + tmp[7] * src[9];
        dst[15] = tmp[10] * src[10] + tmp[4] * src[8] + tmp[9] * src[9];
        dst[15] -= tmp[8] * src[9] + tmp[11] * src[10] + tmp[5] * src[8];

        /* calculate determinant */
        det = src[0] * dst[0] + src[1] * dst[1] + src[2] * dst[2] + src[3] * dst[3];

        if (det == 0.0f) {
            throw new RuntimeException("singular matrix is not invertible");
        }

        /* calculate matrix inverse */
        det = 1 / det;

        for (int j = 0; j < 16; j++) {
            dst[j] *= det;
        }

        return ret;
    }

    /**
     * Get the array of 16 matrix values. This returns the internal represenatation
     * of the matrix in OpenGL compatible column-major format.
     *
     * @return The array of matrix values.
     */
    public double[] asArray() {
        return values.clone();
    }

    /**
     * Construct a new Matrix containing only the rotation.
     *
     * @return The newly constructed matrix.
     */
    public Mat4 getRotation() {
        Mat4 r = new Mat4();
        r.set(0, 0, get(0, 0));
        r.set(1, 0, get(1, 0));
        r.set(2, 0, get(2, 0));
        r.set(0, 1, get(0, 1));
        r.set(1, 1, get(1, 1));
        r.set(2, 1, get(2, 1));
        r.set(0, 2, get(0, 2));
        r.set(1, 2, get(1, 2));
        r.set(2, 2, get(2, 2));
        return r;
    }

    /**
     * Construct a new matrix containing only the translation.
     *
     * @return The newly constructed matrix.
     */
    public Mat4 getTranslation() {
        Mat4 t = new Mat4();
        t.set(3, 0, get(3, 0));
        t.set(3, 1, get(3, 1));
        t.set(3, 2, get(3, 2));
        return t;
    }

    /**
     * Construct a new Vec3 containing the translational elements.
     *
     * @return The newly constructed Vec3.
     */
    public Vec3 getPosition() {
        return vec3(get(3, 0), get(3, 1), get(3, 2));
    }

    /*
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String s = "";
        for (int r = 0; r < 4; r++) {
            s += "( ";
            for (int c = 0; c < 4; c++) {
                s += get(c, r) + " ";
            }
            s += ")\n";
        }
        return s;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Mat4))
            return false;
        if (o == this)
            return true;
        Mat4 m = (Mat4) o;
        for (int i = 0; i != 16; i++)
            if (values[i] != m.values[i])
                return false;
        return true;
    }

    public boolean equals(Mat4 m, double epsilon) {
        for (int i = 0; i != 16; i++)
            if (Math.abs(values[i] - m.values[i]) > epsilon)
                return false;
        return true;
    }
}
