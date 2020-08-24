package xyz.kamefrede.rpsideas.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

// Boilerplate code taken with love from Vazkii's Quark mod and JamiesWhiteShirt's Clothesline
// Quark is distributed at https://github.com/Vazkii/Quark
// Clothesline is distributed at https://github.com/JamiesWhiteShirt/clothesline

public class RPSTransformer implements IClassTransformer, Opcodes {

    private static final String ASM_HOOKS = "com/kamefrede/rpsideas/asm/RPSAsmHooks";
    private static final Map<String, Transformer> transformers = new HashMap<>();

    static {
        transformers.put("net.minecraft.entity.EntityLivingBase", RPSTransformer::transformElytraChecks);
        transformers.put("net.minecraft.client.entity.EntityPlayerSP", RPSTransformer::transformElytraChecks);
        transformers.put("net.minecraft.client.renderer.entity.layers.LayerCape", RPSTransformer::transformElytraChecks);
        transformers.put("net.minecraft.network.NetHandlerPlayServer", RPSTransformer::transformElytraChecks);
    }

    private static byte[] transformElytraChecks(byte[] basicClass) {
        FieldSignature elytra = new FieldSignature("ELYTRA", "field_185160_cR", "Lnet/minecraft/item/Item;");

        return transformAll(basicClass, "Elytra check hijack",
                combine(node -> {
                    if (!(node instanceof JumpInsnNode))
                        return false;

                    AbstractInsnNode prev = node.getPrevious();

                    return prev.getOpcode() == GETSTATIC && elytra.matches((FieldInsnNode) prev);
                }, (methodNode, node) -> {
                    methodNode.instructions.insertBefore(node.getPrevious(),
                            new MethodInsnNode(INVOKESTATIC, ASM_HOOKS, "fakeElytra",
                                    "(Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;", false));

                    return true;
                }));
    }


    // BOILERPLATE =====================================================================================================

    public static byte[] transformAll(byte[] basicClass, String simpleDesc, MethodAction action) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        log("Applying Transformation to all methods");
        log("Attempting to insert: " + simpleDesc);
        boolean didAnything = patchAllMethods(node, action);

        if (didAnything) {
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            node.accept(writer);
            return writer.toByteArray();
        }

        return basicClass;
    }

    public static boolean patchAllMethods(ClassNode node, MethodAction pred) {
        boolean didAny = false;

        for (MethodNode method : node.methods) didAny |= pred.test(method);

        return didAny;
    }

    public static MethodAction combine(NodeFilter filter, NodeAction action) {
        return (MethodNode node) -> applyOnNode(node, filter, action);
    }

    public static boolean applyOnNode(MethodNode method, NodeFilter filter, NodeAction action) {
        AbstractInsnNode[] nodes = method.instructions.toArray();
        Iterator<AbstractInsnNode> iterator = new InsnArrayIterator(nodes);

        boolean didAny = false;
        while (iterator.hasNext()) {
            AbstractInsnNode anode = iterator.next();
            if (filter.test(anode)) {
                didAny = true;
                if (action.test(method, anode))
                    break;
            }
        }

        return didAny;
    }

    public static void log(String str) {
        LogManager.getLogger("RPS ASM").info(str);
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformers.containsKey(transformedName)) {
            String[] arr = transformedName.split("\\.");
            log("Transforming " + arr[arr.length - 1]);
            return transformers.get(transformedName).apply(basicClass);
        }

        return basicClass;
    }

    public interface Transformer extends Function<byte[], byte[]> {
        // NO-OP
    }

    public interface MethodAction extends Predicate<MethodNode> {
        // NO-OP
    }

    // Basic interface aliases to not have to clutter up the code with generics over and over again

    public interface NodeFilter extends Predicate<AbstractInsnNode> {
        // NO-OP
    }

    public interface NodeAction extends BiPredicate<MethodNode, AbstractInsnNode> {
        // NO-OP
    }

    private static class InsnArrayIterator implements ListIterator<AbstractInsnNode> {

        private final AbstractInsnNode[] array;
        private int index;

        public InsnArrayIterator(AbstractInsnNode[] array) {
            this(array, 0);
        }

        public InsnArrayIterator(AbstractInsnNode[] array, int index) {
            this.array = array;
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            return array.length > index + 1 && index >= 0;
        }

        @Override
        public AbstractInsnNode next() {
            if (hasNext())
                return array[++index];
            return null;
        }

        @Override
        public boolean hasPrevious() {
            return index > 0 && index <= array.length;
        }

        @Override
        public AbstractInsnNode previous() {
            if (hasPrevious())
                return array[--index];
            return null;
        }

        @Override
        public int nextIndex() {
            return hasNext() ? index + 1 : array.length;
        }

        @Override
        public int previousIndex() {
            return hasPrevious() ? index - 1 : 0;
        }

        @Override
        public void remove() {
            throw new Error("Unimplemented");
        }

        @Override
        public void set(AbstractInsnNode e) {
            throw new Error("Unimplemented");
        }

        @Override
        public void add(AbstractInsnNode e) {
            throw new Error("Unimplemented");
        }
    }

    public static class FieldSignature {
        private final String fieldName, srgName, fieldDesc;

        public FieldSignature(String fieldName, String srgName, String fieldDesc) {
            this.fieldName = fieldName;
            this.srgName = srgName;
            this.fieldDesc = fieldDesc;
        }

        @Override
        public String toString() {
            return "Names [" + fieldName + ", " + srgName + "] Descriptor " + fieldDesc;
        }

        public boolean matches(String fieldName, String fieldDesc) {
            return (fieldName.equals(this.fieldName) || fieldName.equals(srgName))
                    && (fieldDesc.equals(this.fieldDesc));
        }

        public boolean matches(FieldInsnNode field) {
            return matches(field.name, field.desc);
        }
    }
}
