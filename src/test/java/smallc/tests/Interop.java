package smallc.tests;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

import smallc.ISmallcc;
import smallc.Factory;

public class Interop {

    private static final String BASE_DIR  = System.getProperty("basedir");
    private static final String SRC_RES = BASE_DIR + "/src/test/resources";
    private static final String TGT_RES = BASE_DIR + "/target/classes";

    @Test
    public void instance() {
        ISmallcc cc = Factory.getInstance();
        Assert.assertNotNull(cc);
        Assert.assertTrue(cc instanceof ISmallcc);
    }

    // Do nothing.

    @Test
    public void interop_001() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_001.c");
        Assert.assertTrue(code == 0);
    }

    // Literal null cannot be #defined.

    @Test
    public void interop_002() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_002.c");
        Assert.assertTrue(code == 3);
    }

    // Incorrect array dimensions.

    @Test
    public void interop_003() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_003.c");
        Assert.assertTrue(code == 4);
    }

    // Symbol cannot be redefined.

    @Test
    public void interop_004() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_004.c");
        Assert.assertTrue(code == 5);
    }

    // Global variable cannot be redefined.

    @Test
    public void interop_005() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_005.c");
        Assert.assertTrue(code == 5);
    }

    // Function cannot be redefined.

    @Test
    public void interop_006() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_006.c");
        Assert.assertTrue(code == 5);
    }

    // Cannot redefine formal parameters.

    @Test
    public void interop_007() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_007.c");
        Assert.assertTrue(code == 5);
    }

    // Test for correct signatures.

    @Test
    public void interop_008() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_008.c");
        Assert.assertTrue(code == 0);
    }

    // Cannot redefine dimensions in return value.

    @Test
    public void interop_009() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_009.c");
        Assert.assertTrue(code == 2);
    }

    // Cannot redefine dimensions in formal parameters.

    @Test
    public void interop_010() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_010.c");
        Assert.assertTrue(code == 2);
    }

    // Compile and call functions.

    @Test
    public void interop_011() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_011.c");
        Assert.assertTrue(code == 0);
    }

    @Test
    public void interop_011_add() throws Throwable {
        Class<?> clazz = Class.forName("interop_011");
        Method add = clazz.getMethod("add", Integer.TYPE, Integer.TYPE);
        Integer r = (Integer) add.invoke(null, 3, 4);
        Assert.assertTrue(r.intValue() == 7);
    }

    @Test
    public void interop_011_sum() throws Throwable {
        Class<?> clazz = Class.forName("interop_011");
        Method sum = clazz.getMethod("sum", Integer.TYPE, Integer.TYPE);
        Integer r = (Integer) sum.invoke(null, 3, 4);
        Assert.assertTrue(r.intValue() == 7);
    }

    // Expressions in global symbols and variables.

    @Test
    public void interop_012() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_012.c");
        Assert.assertTrue(code == 0);
    }

    // Automatic initialization of local variables.

    @Test
    public void interop_013() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_013.c");
        Assert.assertTrue(code == 0);
    }

    // Integer arithmetic.

    @Test
    public void interop_014() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_014.c");
        Assert.assertTrue(code == 0);
    }

    @Test
    public void interop_014_add() throws Throwable {
        Class<?> clazz = Class.forName("interop_014");
        Method add = clazz.getMethod("add", Integer.TYPE, Integer.TYPE);
        Integer r = (Integer) add.invoke(null, 3, 4);
        Assert.assertTrue(r.intValue() == 7);
    }

    @Test
    public void interop_014_plus() throws Throwable {
        Class<?> clazz = Class.forName("interop_014");
        Method plus = clazz.getMethod("plus", Integer.TYPE, Integer.TYPE);
        Integer r = (Integer) plus.invoke(null, 4, 3);
        Assert.assertTrue(r.intValue() == 7);
    }

    @Test
    public void interop_014_sub() throws Throwable {
        Class<?> clazz = Class.forName("interop_014");
        Method sub = clazz.getMethod("sub", Integer.TYPE, Integer.TYPE);
        Integer r = (Integer) sub.invoke(null, 7, 4);
        Assert.assertTrue(r.intValue() == 3);
    }

    @Test
    public void interop_014_minus() throws Throwable {
        Class<?> clazz = Class.forName("interop_014");
        Method minus = clazz.getMethod("minus", Integer.TYPE, Integer.TYPE);
        Integer r = (Integer) minus.invoke(null, 7, 3);
        Assert.assertTrue(r.intValue() == 4);
    }

    @Test
    public void interop_014_mul() throws Throwable {
        Class<?> clazz = Class.forName("interop_014");
        Method mul = clazz.getMethod("mul", Integer.TYPE, Integer.TYPE);
        Integer r = (Integer) mul.invoke(null, 7, 4);
        Assert.assertTrue(r.intValue() == 28);
    }

    @Test
    public void interop_014_star() throws Throwable {
        Class<?> clazz = Class.forName("interop_014");
        Method star = clazz.getMethod("star", Integer.TYPE, Integer.TYPE);
        Integer r = (Integer) star.invoke(null, 4, 7);
        Assert.assertTrue(r.intValue() == 28);
    }

    @Test
    public void interop_014_div() throws Throwable {
        Class<?> clazz = Class.forName("interop_014");
        Method div = clazz.getMethod("div", Integer.TYPE, Integer.TYPE);
        Integer r = (Integer) div.invoke(null, 144, 12);
        Assert.assertTrue(r.intValue() == 12);
    }

    @Test
    public void interop_014_slash() throws Throwable {
        Class<?> clazz = Class.forName("interop_014");
        Method slash = clazz.getMethod("slash", Integer.TYPE, Integer.TYPE);
        Integer r = (Integer) slash.invoke(null, 1024, 2);
        Assert.assertTrue(r.intValue() == 512);
    }

    @Test
    public void interop_014_mod() throws Throwable {
        Class<?> clazz = Class.forName("interop_014");
        Method mod = clazz.getMethod("mod", Integer.TYPE, Integer.TYPE);
        Integer r = (Integer) mod.invoke(null, 14, 3);
        Assert.assertTrue(r.intValue() == 2);
    }

    @Test
    public void interop_014_percent() throws Throwable {
        Class<?> clazz = Class.forName("interop_014");
        Method percent = clazz.getMethod("percent", Integer.TYPE, Integer.TYPE);
        Integer r = (Integer) percent.invoke(null, 20, 6);
        Assert.assertTrue(r.intValue() == 2);
    }

    // Long arithmetic.

    @Test
    public void interop_015() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_015.c");
        Assert.assertTrue(code == 0);
    }

    @Test
    public void interop_015_add() throws Throwable {
        Class<?> clazz = Class.forName("interop_015");
        Method add = clazz.getMethod("add", Long.TYPE, Long.TYPE);
        Long r = (Long) add.invoke(null, 3L, 4L);
        Assert.assertTrue(r.longValue() == 7L);
    }

    @Test
    public void interop_015_plus() throws Throwable {
        Class<?> clazz = Class.forName("interop_015");
        Method plus = clazz.getMethod("plus", Long.TYPE, Long.TYPE);
        Long r = (Long) plus.invoke(null, 4L, 3L);
        Assert.assertTrue(r.longValue() == 7L);
    }

    @Test
    public void interop_015_sub() throws Throwable {
        Class<?> clazz = Class.forName("interop_015");
        Method sub = clazz.getMethod("sub", Long.TYPE, Long.TYPE);
        Long r = (Long) sub.invoke(null, 7L, 4L);
        Assert.assertTrue(r.longValue() == 3L);
    }

    @Test
    public void interop_015_minus() throws Throwable {
        Class<?> clazz = Class.forName("interop_015");
        Method minus = clazz.getMethod("minus", Long.TYPE, Long.TYPE);
        Long r = (Long) minus.invoke(null, 7L, 3L);
        Assert.assertTrue(r.longValue() == 4L);
    }

    @Test
    public void interop_015_mul() throws Throwable {
        Class<?> clazz = Class.forName("interop_015");
        Method mul = clazz.getMethod("mul", Long.TYPE, Long.TYPE);
        Long r = (Long) mul.invoke(null, 7L, 4L);
        Assert.assertTrue(r.longValue() == 28L);
    }

    @Test
    public void interop_015_star() throws Throwable {
        Class<?> clazz = Class.forName("interop_015");
        Method star = clazz.getMethod("star", Long.TYPE, Long.TYPE);
        Long r = (Long) star.invoke(null, 4L, 7L);
        Assert.assertTrue(r.longValue() == 28L);
    }

    @Test
    public void interop_015_div() throws Throwable {
        Class<?> clazz = Class.forName("interop_015");
        Method div = clazz.getMethod("div", Long.TYPE, Long.TYPE);
        Long r = (Long) div.invoke(null, 144L, 12L);
        Assert.assertTrue(r.longValue() == 12L);
    }

    @Test
    public void interop_015_slash() throws Throwable {
        Class<?> clazz = Class.forName("interop_015");
        Method slash = clazz.getMethod("slash", Long.TYPE, Long.TYPE);
        Long r = (Long) slash.invoke(null, 1024L, 2L);
        Assert.assertTrue(r.longValue() == 512L);
    }

    @Test
    public void interop_015_mod() throws Throwable {
        Class<?> clazz = Class.forName("interop_015");
        Method mod = clazz.getMethod("mod", Long.TYPE, Long.TYPE);
        Long r = (Long) mod.invoke(null, 14L, 3L);
        Assert.assertTrue(r.longValue() == 2L);
    }

    @Test
    public void interop_015_percent() throws Throwable {
        Class<?> clazz = Class.forName("interop_015");
        Method percent = clazz.getMethod("percent", Long.TYPE, Long.TYPE);
        Long r = (Long) percent.invoke(null, 14L, 3L);
        Assert.assertTrue(r.longValue() == 2L);
    }

    // Float arithmetic.

    @Test
    public void interop_016() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_016.c");
        Assert.assertTrue(code == 0);
    }

    @Test
    public void interop_016_add() throws Throwable {
        Class<?> clazz = Class.forName("interop_016");
        Method add = clazz.getMethod("add", Float.TYPE, Float.TYPE);
        Float r = (Float) add.invoke(null, 3.0f, 4.0f);
        Assert.assertTrue(r.floatValue() == 7.0f);
    }

    @Test
    public void interop_016_plus() throws Throwable {
        Class<?> clazz = Class.forName("interop_016");
        Method plus = clazz.getMethod("plus", Float.TYPE, Float.TYPE);
        Float r = (Float) plus.invoke(null, 4.0f, 3.0f);
        Assert.assertTrue(r.floatValue() == 7.0f);
    }

    @Test
    public void interop_016_sub() throws Throwable {
        Class<?> clazz = Class.forName("interop_016");
        Method sub = clazz.getMethod("sub", Float.TYPE, Float.TYPE);
        Float r = (Float) sub.invoke(null, 7.0f, 4.0f);
        Assert.assertTrue(r.floatValue() == 3.0f);
    }

    @Test
    public void interop_016_minus() throws Throwable {
        Class<?> clazz = Class.forName("interop_016");
        Method minus = clazz.getMethod("minus", Float.TYPE, Float.TYPE);
        Float r = (Float) minus.invoke(null, 7.0f, 3.0f);
        Assert.assertTrue(r.floatValue() == 4.0f);
    }

    @Test
    public void interop_016_mul() throws Throwable {
        Class<?> clazz = Class.forName("interop_016");
        Method mul = clazz.getMethod("mul", Float.TYPE, Float.TYPE);
        Float r = (Float) mul.invoke(null, 7.0f, 4.0f);
        Assert.assertTrue(r.floatValue() == 28.0f);
    }

    @Test
    public void interop_016_star() throws Throwable {
        Class<?> clazz = Class.forName("interop_016");
        Method star = clazz.getMethod("star", Float.TYPE, Float.TYPE);
        Float r = (Float) star.invoke(null, 4.0f, 7.0f);
        Assert.assertTrue(r.floatValue() == 28.0f);
    }

    @Test
    public void interop_016_div() throws Throwable {
        Class<?> clazz = Class.forName("interop_016");
        Method div = clazz.getMethod("div", Float.TYPE, Float.TYPE);
        Float r = (Float) div.invoke(null, 144.0f, 12.0f);
        Assert.assertTrue(r.floatValue() == 12.0f);
    }

    @Test
    public void interop_016_slash() throws Throwable {
        Class<?> clazz = Class.forName("interop_016");
        Method slash = clazz.getMethod("slash", Float.TYPE, Float.TYPE);
        Float r = (Float) slash.invoke(null, 1024.0f, 2.0f);
        Assert.assertTrue(r.floatValue() == 512.0f);
    }

    @Test
    public void interop_016_mod() throws Throwable {
        Class<?> clazz = Class.forName("interop_016");
        Method mod = clazz.getMethod("mod", Float.TYPE, Float.TYPE);
        Float r = (Float) mod.invoke(null, 14.0f, 3.0f);
        Assert.assertTrue(r.floatValue() == 2.0f);
    }

    @Test
    public void interop_016_percent() throws Throwable {
        Class<?> clazz = Class.forName("interop_016");
        Method percent = clazz.getMethod("percent", Float.TYPE, Float.TYPE);
        Float r = (Float) percent.invoke(null, 14.0f, 3.0f);
        Assert.assertTrue(r.floatValue() == 2.0f);
    }

    // Double arithmetic.

    @Test
    public void interop_017() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_017.c");
        Assert.assertTrue(code == 0);
    }

    @Test
    public void interop_017_add() throws Throwable {
        Class<?> clazz = Class.forName("interop_017");
        Method add = clazz.getMethod("add", Double.TYPE, Double.TYPE);
        Double r = (Double) add.invoke(null, 3.0, 4.0);
        Assert.assertTrue(r.doubleValue() == 7.0);
    }

    @Test
    public void interop_017_plus() throws Throwable {
        Class<?> clazz = Class.forName("interop_017");
        Method plus = clazz.getMethod("plus", Double.TYPE, Double.TYPE);
        Double r = (Double) plus.invoke(null, 4.0, 3.0);
        Assert.assertTrue(r.doubleValue() == 7.0);
    }

    @Test
    public void interop_017_sub() throws Throwable {
        Class<?> clazz = Class.forName("interop_017");
        Method sub = clazz.getMethod("sub", Double.TYPE, Double.TYPE);
        Double r = (Double) sub.invoke(null, 7.0, 4.0);
        Assert.assertTrue(r.doubleValue() == 3.0);
    }

    @Test
    public void interop_017_minus() throws Throwable {
        Class<?> clazz = Class.forName("interop_017");
        Method minus = clazz.getMethod("minus", Double.TYPE, Double.TYPE);
        Double r = (Double) minus.invoke(null, 7.0, 3.0);
        Assert.assertTrue(r.doubleValue() == 4.0);
    }

    @Test
    public void interop_017_mul() throws Throwable {
        Class<?> clazz = Class.forName("interop_017");
        Method mul = clazz.getMethod("mul", Double.TYPE, Double.TYPE);
        Double r = (Double) mul.invoke(null, 7.0, 4.0);
        Assert.assertTrue(r.doubleValue() == 28.0);
    }

    @Test
    public void interop_017_star() throws Throwable {
        Class<?> clazz = Class.forName("interop_017");
        Method star = clazz.getMethod("star", Double.TYPE, Double.TYPE);
        Double r = (Double) star.invoke(null, 4.0, 7.0);
        Assert.assertTrue(r.doubleValue() == 28.0);
    }

    @Test
    public void interop_017_div() throws Throwable {
        Class<?> clazz = Class.forName("interop_017");
        Method div = clazz.getMethod("div", Double.TYPE, Double.TYPE);
        Double r = (Double) div.invoke(null, 144.0, 12.0);
        Assert.assertTrue(r.doubleValue() == 12.0);
    }

    @Test
    public void interop_017_slash() throws Throwable {
        Class<?> clazz = Class.forName("interop_017");
        Method slash = clazz.getMethod("slash", Double.TYPE, Double.TYPE);
        Double r = (Double) slash.invoke(null, 1024.0, 2.0);
        Assert.assertTrue(r.doubleValue() == 512.0);
    }

    @Test
    public void interop_017_mod() throws Throwable {
        Class<?> clazz = Class.forName("interop_017");
        Method mod = clazz.getMethod("mod", Double.TYPE, Double.TYPE);
        Double r = (Double) mod.invoke(null, 14.0, 3.0);
        Assert.assertTrue(r.doubleValue() == 2.0);
    }

    @Test
    public void interop_017_percent() throws Throwable {
        Class<?> clazz = Class.forName("interop_017");
        Method percent = clazz.getMethod("percent", Double.TYPE, Double.TYPE);
        Double r = (Double) percent.invoke(null, 14.0, 3.0);
        Assert.assertTrue(r.doubleValue() == 2.0);
    }

    // Invalid return value.

    @Test
    public void interop_018() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_018.c");
        Assert.assertTrue(code == 9);
    }

    // A return value is expected.

    @Test
    public void interop_019() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_019.c");
        Assert.assertTrue(code == 8);
    }

    // Function reference must be function call.

    @Test
    public void interop_020() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_020.c");
        Assert.assertTrue(code == 7);
    }

    // Mixed operators.

    @Test
    public void interop_021() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_021.c");
        Assert.assertTrue(code == 0);
    }

    @Test
    public void interop_021_calc1() throws Throwable {
        Class<?> clazz = Class.forName("interop_021");
        Method calc1 = clazz.getMethod("calc1");
        Integer r = (Integer) calc1.invoke(null);
        Assert.assertTrue(r.intValue() == 18);
    }

    @Test
    public void interop_021_calc2() throws Throwable {
        Class<?> clazz = Class.forName("interop_021");
        Method calc2 = clazz.getMethod("calc2");
        Double r = (Double) calc2.invoke(null);
        Assert.assertTrue(r.doubleValue() == 6.25);
    }

    // Unary plus and minus.

    @Test
    public void interop_022() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_022.c");
        Assert.assertTrue(code == 0);
    }

    @Test
    public void interop_022_pluslit() throws Throwable {
        Class<?> clazz = Class.forName("interop_022");
        Method pl = clazz.getMethod("pluslit");
        Integer r = (Integer) pl.invoke(null);
        Assert.assertTrue(r.intValue() == 1);
    }

    @Test
    public void interop_022_neglit() throws Throwable {
        Class<?> clazz = Class.forName("interop_022");
        Method nl = clazz.getMethod("neglit");
        Integer r = (Integer) nl.invoke(null);
        Assert.assertTrue(r.intValue() == -1);
    }

    @Test
    public void interop_022_plusvar() throws Throwable {
        Class<?> clazz = Class.forName("interop_022");
        Method pv = clazz.getMethod("plusvar");
        Integer r = (Integer) pv.invoke(null);
        Assert.assertTrue(r.intValue() == 500);
    }

    @Test
    public void interop_022_negvar() throws Throwable {
        Class<?> clazz = Class.forName("interop_022");
        Method nv = clazz.getMethod("negvar");
        Integer r = (Integer) nv.invoke(null);
        Assert.assertTrue(r.intValue() == -500);
    }

    @Test
    public void interop_022_multlit() throws Throwable {
        Class<?> clazz = Class.forName("interop_022");
        Method mul = clazz.getMethod("multlit");
        Long r = (Long) mul.invoke(null);
        Assert.assertTrue(r.longValue() == -50);
    }

    @Test
    public void interop_022_multvar() throws Throwable {
        Class<?> clazz = Class.forName("interop_022");
        Method mul = clazz.getMethod("multvar");
        Long r = (Long) mul.invoke(null);
        Assert.assertTrue(r.longValue() == 25);
    }

    @Test
    public void interop_022_negexpr() throws Throwable {
        Class<?> clazz = Class.forName("interop_022");
        Method neg = clazz.getMethod("negexpr");
        Long r = (Long) neg.invoke(null);
        Assert.assertTrue(r.longValue() == -4);
    }

    // Shift operators.

    @Test
    public void interop_023() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_023.c");
        Assert.assertTrue(code == 0);
    }

    @Test
    public void interop_023_left8() throws Throwable {
        Class<?> clazz = Class.forName("interop_023");
        Method left = clazz.getMethod("left8");
        Integer r = (Integer) left.invoke(null);
        Assert.assertTrue(r.intValue() == (1 << 8));
    }

    @Test
    public void interop_023_right4() throws Throwable {
        Class<?> clazz = Class.forName("interop_023");
        Method right = clazz.getMethod("right4");
        Integer r = (Integer) right.invoke(null);
        Assert.assertTrue(r.intValue() == (1024 >> 4));
    }

    @Test
    public void interop_023_uright2() throws Throwable {
        Class<?> clazz = Class.forName("interop_023");
        Method right = clazz.getMethod("uright2");
        Integer r = (Integer) right.invoke(null);
        Assert.assertTrue(r.intValue() == (1024 >>> 2));
    }

    // Bitwise logical operators.

    @Test
    public void interop_024() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_024.c");
        Assert.assertTrue(code == 0);
    }

    @Test
    public void interop_024_iand() throws Throwable {
        Class<?> clazz = Class.forName("interop_024");
        Method iand = clazz.getMethod("iand");
        Integer r = (Integer) iand.invoke(null);
        Assert.assertTrue(r.intValue() == (0xF0 & 0x31));
    }

    @Test
    public void interop_024_ior() throws Throwable {
        Class<?> clazz = Class.forName("interop_024");
        Method ior = clazz.getMethod("ior");
        Integer r = (Integer) ior.invoke(null);
        Assert.assertTrue(r.intValue() == (0x0F | 0xF0));
    }

    @Test
    public void interop_024_inot() throws Throwable {
        Class<?> clazz = Class.forName("interop_024");
        Method inot = clazz.getMethod("inot");
        Integer r = (Integer) inot.invoke(null);
        Assert.assertTrue(r.intValue() == ~0x0F0F);
    }

    @Test
    public void interop_024_ixor() throws Throwable {
        Class<?> clazz = Class.forName("interop_024");
        Method ixor = clazz.getMethod("ixor");
        Integer r = (Integer) ixor.invoke(null);
        Assert.assertTrue(r.intValue() == (0x05 ^ 0x10));
    }

    @Test
    public void interop_024_land() throws Throwable {
        Class<?> clazz = Class.forName("interop_024");
        Method land = clazz.getMethod("land");
        Long r = (Long) land.invoke(null);
        Assert.assertTrue(r.longValue() == (0xF0L & 0x31L));
    }

    @Test
    public void interop_024_lor() throws Throwable {
        Class<?> clazz = Class.forName("interop_024");
        Method lor = clazz.getMethod("lor");
        Long r = (Long) lor.invoke(null);
        Assert.assertTrue(r.longValue() == (0x0FL | 0xF0L));
    }

    @Test
    public void interop_024_lnot() throws Throwable {
        Class<?> clazz = Class.forName("interop_024");
        Method lnot = clazz.getMethod("lnot");
        Long r = (Long) lnot.invoke(null);
        Assert.assertTrue(r.longValue() == ~0x0F0FL);
    }

    @Test
    public void interop_024_lxor() throws Throwable {
        Class<?> clazz = Class.forName("interop_024");
        Method lxor = clazz.getMethod("lxor");
        Long r = (Long) lxor.invoke(null);
        Assert.assertTrue(r.longValue() == (0x05L ^ 0x10L));
    }

    @Test
    public void interop_024_iand_var() throws Throwable {
        Class<?> clazz = Class.forName("interop_024");
        Method iand = clazz.getMethod("iand_var", Integer.TYPE, Integer.TYPE);
        Integer r = (Integer) iand.invoke(null, 0xF0, 0x31);
        Assert.assertTrue(r.intValue() == (0xF0 & 0x31));
    }

    @Test
    public void interop_024_ior_var() throws Throwable {
        Class<?> clazz = Class.forName("interop_024");
        Method ior = clazz.getMethod("ior_var", Integer.TYPE, Integer.TYPE);
        Integer r = (Integer) ior.invoke(null,0x0F, 0xF0);
        Assert.assertTrue(r.intValue() == (0x0F | 0xF0));
    }

    @Test
    public void interop_024_inot_var() throws Throwable {
        Class<?> clazz = Class.forName("interop_024");
        Method inot = clazz.getMethod("inot_var", Integer.TYPE);
        Integer r = (Integer) inot.invoke(null, 0x0F0F);
        Assert.assertTrue(r.intValue() == ~0x0F0F);
    }

    @Test
    public void interop_024_ixor_var() throws Throwable {
        Class<?> clazz = Class.forName("interop_024");
        Method ixor = clazz.getMethod("ixor_var", Integer.TYPE, Integer.TYPE);
        Integer r = (Integer) ixor.invoke(null, 0x05, 0x10);
        Assert.assertTrue(r.intValue() == (0x05 ^ 0x10));
    }

    @Test
    public void interop_024_land_var() throws Throwable {
        Class<?> clazz = Class.forName("interop_024");
        Method land = clazz.getMethod("land_var", Long.TYPE, Long.TYPE);
        Long r = (Long) land.invoke(null, 0xF0L, 0x31L);
        Assert.assertTrue(r.longValue() == (0xF0L & 0x31L));
    }

    @Test
    public void interop_024_lor_var() throws Throwable {
        Class<?> clazz = Class.forName("interop_024");
        Method lor = clazz.getMethod("lor_var", Long.TYPE, Long.TYPE);
        Long r = (Long) lor.invoke(null, 0x0FL, 0xF0L);
        Assert.assertTrue(r.longValue() == (0x0FL | 0xF0L));
    }

    @Test
    public void interop_024_lnot_var() throws Throwable {
        Class<?> clazz = Class.forName("interop_024");
        Method lnot = clazz.getMethod("lnot_var", Long.TYPE);
        Long r = (Long) lnot.invoke(null, 0x0F0FL);
        Assert.assertTrue(r.longValue() == ~0x0F0FL);
    }

    @Test
    public void interop_024_lxor_var() throws Throwable {
        Class<?> clazz = Class.forName("interop_024");
        Method lxor = clazz.getMethod("lxor_var", Long.TYPE, Long.TYPE);
        Long r = (Long) lxor.invoke(null, 0x05L, 0x10L);
        Assert.assertTrue(r.longValue() == (0x05L ^ 0x10L));
    }

    // Increment and decrement.

    @Test
    public void interop_025() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_025.c");
        Assert.assertTrue(code == 0);
    }

    @Test
    public void interop_025_inc_globals() throws Throwable {
        Class<?> clazz = Class.forName("interop_025");
        Method inc = clazz.getMethod("inc_globals");
        Integer r = (Integer) inc.invoke(null);
        Assert.assertTrue(r.intValue() == 7);
    }

    @Test
    public void interop_025_dec_globals() throws Throwable {
        Class<?> clazz = Class.forName("interop_025");
        Method dec  = clazz.getMethod("dec_globals");
        Integer r = (Integer) dec.invoke(null);
        Assert.assertTrue(r.intValue() == 4);
    }

    @Test
    public void interop_025_inc_locals() throws Throwable {
        Class<?> clazz = Class.forName("interop_025");
        Method inc = clazz.getMethod("inc_locals");
        Double r = (Double) inc.invoke(null);
        Assert.assertTrue(r.doubleValue() == 2.000033339999);
    }

    @Test
    public void interop_025_dec_locals() throws Throwable {
        Class<?> clazz = Class.forName("interop_025");
        Method dec = clazz.getMethod("dec_locals");
        Short r = (Short) dec.invoke(null);
        Assert.assertTrue(r.shortValue() == 0);
    }

    @Test
    public void interop_025_inc() throws Throwable {
        Class<?> clazz = Class.forName("interop_025");
        Method inc = clazz.getMethod("inc");
        Integer r = (Integer) inc.invoke(null);
        Assert.assertTrue(r.intValue() == 10000);
    }

    @Test
    public void interop_025_dec() throws Throwable {
        Class<?> clazz = Class.forName("interop_025");
        Method dec = clazz.getMethod("dec");
        Integer r = (Integer) dec.invoke(null);
        Assert.assertTrue(r.intValue() == 9999);
    }

    @Test
    public void interop_025_inc_arg() throws Throwable {
        Class<?> clazz = Class.forName("interop_025");
        Method inc = clazz.getMethod("inc_arg", Long.TYPE);
        Long r = (Long) inc.invoke(null, 9999L);
        Assert.assertTrue(r.longValue() == 10000L);
    }

    @Test
    public void interop_025_dec_arg() throws Throwable {
        Class<?> clazz = Class.forName("interop_025");
        Method dec = clazz.getMethod("dec_arg", Long.TYPE);
        Long r = (Long) dec.invoke(null, 10000L);
        Assert.assertTrue(r.longValue() == 9999L);
    }

    @Test
    public void interop_025_incpost() throws Throwable {
        Class<?> clazz = Class.forName("interop_025");
        Method inc = clazz.getMethod("incpost");
        Integer r = (Integer) inc.invoke(null);
        Assert.assertTrue(r.intValue() == 9999);
    }

    @Test
    public void interop_025_decpost() throws Throwable {
        Class<?> clazz = Class.forName("interop_025");
        Method dec = clazz.getMethod("decpost");
        Integer r = (Integer) dec.invoke(null);
        Assert.assertTrue(r.intValue() == 10000);
    }

    @Test
    public void interop_025_incpost_arg() throws Throwable {
        Class<?> clazz = Class.forName("interop_025");
        Method inc = clazz.getMethod("incpost_arg", Long.TYPE);
        Long r = (Long) inc.invoke(null, 9999L);
        Assert.assertTrue(r.longValue() == 9999L);
    }

    @Test
    public void interop_025_decpost_arg() throws Throwable {
        Class<?> clazz = Class.forName("interop_025");
        Method dec = clazz.getMethod("decpost_arg", Long.TYPE);
        Long r = (Long) dec.invoke(null, 10000L);
        Assert.assertTrue(r.longValue() == 10000L);
    }

    // Assignment

    @Test
    public void interop_026() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_026.c");
        Assert.assertTrue(code == 0);
    }

    @Test
    public void interop_026_sum() throws Throwable {
        Class<?> clazz = Class.forName("interop_026");
        Method sum = clazz.getMethod("sum");
        Integer r = (Integer) sum.invoke(null);
        Assert.assertTrue(r.intValue() == 7);
    }

    @Test
    public void interop_026_sum_args() throws Throwable {
        Class<?> clazz = Class.forName("interop_026");
        Method sum = clazz.getMethod("sum_args", Integer.TYPE, Integer.TYPE);
        Integer r = (Integer) sum.invoke(null, 3, 4);
        Assert.assertTrue(r.intValue() == 7);
    }

    @Test
    public void interop_026_multiple() throws Throwable {
        Class<?> clazz = Class.forName("interop_026");
        Method multiple = clazz.getMethod("multiple");
        Long r = (Long) multiple.invoke(null);
        Assert.assertTrue(r.longValue() == 2112000001L);
    }

    @Test
    public void interop_026_global() throws Throwable {
        Class<?> clazz = Class.forName("interop_026");
        Method global = clazz.getMethod("global");
        Integer r = (Integer) global.invoke(null);
        Assert.assertTrue(r.intValue() == 100);
    }

    // Arrays access.

    @Test
    public void interop_027() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_027.c");
        Assert.assertTrue(code == 0);
    }

    @Test
    public void interop_027_single() throws Throwable {
        Class<?> clazz = Class.forName("interop_027");
        Method single = clazz.getMethod("single_dim");
        Integer r = (Integer) single.invoke(null);
        Assert.assertTrue(r.intValue() == 3);
    }

    @Test
    public void interop_027_double() throws Throwable {
        Class<?> clazz = Class.forName("interop_027");
        Method doubleDim = clazz.getMethod("double_dim");
        Integer r = (Integer) doubleDim.invoke(null);
        Assert.assertTrue(r.intValue() == 7);
    }

    @Test
    public void interop_027_triple() throws Throwable {
        Class<?> clazz = Class.forName("interop_027");
        Method triple = clazz.getMethod("triple_dim");
        Integer r = (Integer) triple.invoke(null);
        Assert.assertTrue(r.intValue() == -3);
    }

    @Test
    public void interop_027_INTS() throws Throwable {
        Class<?> clazz = Class.forName("interop_027");
        Method ints = clazz.getMethod("global_INTS");
        Integer r = (Integer) ints.invoke(null);
        Assert.assertTrue(r.intValue() == 7);
    }

    @Test
    public void interop_027_LONGS() throws Throwable {
        Class<?> clazz = Class.forName("interop_027");
        Method longs = clazz.getMethod("global_LONGS");
        Long r = (Long) longs.invoke(null);
        Assert.assertTrue(r.longValue() == 3);
    }

    @Test
    public void interop_027_assign() throws Throwable {
        Class<?> clazz = Class.forName("interop_027");
        Method assign = clazz.getMethod("assign");
        Integer r = (Integer) assign.invoke(null);
        Assert.assertTrue(r.intValue() == 2112);
    }

    @Test
    public void interop_027_massign() throws Throwable {
        Class<?> clazz = Class.forName("interop_027");
        Method assign = clazz.getMethod("massign");
        Integer r = (Integer) assign.invoke(null);
        Assert.assertTrue(r.intValue() == 2112);
    }

    @Test
    public void interop_027_massignm() throws Throwable {
        Class<?> clazz = Class.forName("interop_027");
        Method assign = clazz.getMethod("massignm");
        Integer r = (Integer) assign.invoke(null);
        Assert.assertTrue(r.intValue() == 2112);
    }

    // Indexing a primitive type (a variant of too many indexes).

    @Test
    public void interop_028() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_028.c");
        Assert.assertTrue(code == 11);
    }

    // Too many indexes.

    @Test
    public void interop_029() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_029.c");
        Assert.assertTrue(code == 11);
    }

    // Insufficient indexes.

    @Test
    public void interop_030() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_030.c");
        Assert.assertTrue(code == 11);
    }

    // Function call statement.

    @Test
    public void interop_031() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_031.c");
        Assert.assertTrue(code == 0);
    }

    @Test
    public void interop_031_call_sum() throws Throwable {
        Class<?> clazz = Class.forName("interop_031");
        Method call = clazz.getMethod("call_sum");
        Integer r = (Integer) call.invoke(null);
        Assert.assertTrue(r.intValue() == 7);
    }

    @Test
    public void interop_031_call_nested() throws Throwable {
        Class<?> clazz = Class.forName("interop_031");
        Method call = clazz.getMethod("call_nested");
        Integer r = (Integer) call.invoke(null);
        Assert.assertTrue(r.intValue() == 4224);
    }

    @Test
    public void interop_031_call_sums() throws Throwable {
        Class<?> clazz = Class.forName("interop_031");
        Method call = clazz.getMethod("call_sums");
        Integer r = (Integer) call.invoke(null);
        Assert.assertTrue(r.intValue() == 30);
    }

    // Undeclared identifier.

    @Test
    public void interop_032() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_032.c");
        Assert.assertTrue(code == 6);
    }

    // Undeclared function.

    @Test
    public void interop_033() {
        ISmallcc cc = Factory.getInstance();
        int code = cc.compile(SRC_RES, TGT_RES, ".", "interop_033.c");
        Assert.assertTrue(code == 6);
    }

}
