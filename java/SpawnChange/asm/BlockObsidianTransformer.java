package SpawnChange.asm;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;

/**
 * Created by A.K. on 14/03/22.
 */
public class BlockObsidianTransformer implements IClassTransformer, Opcodes{
    private static final String TARGET_CLASS_NAME = "net.minecraft.block.BlockObsidian";
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!FMLLaunchHandler.side().isClient() || !TARGET_CLASS_NAME.equals(transformedName)) {return basicClass;}
        try {
            SpawnChangeCorePlugin.logger.info("Start " + TARGET_CLASS_NAME + " transform");
            ClassReader classReader = new ClassReader(basicClass);
            ClassWriter classWriter = new ClassWriter(1);
            classReader.accept(new CustomVisitor(name,classWriter), 8);
            SpawnChangeCorePlugin.logger.info("Finish " + TARGET_CLASS_NAME + " transform");
            return classWriter.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("failed : BlockObsidenTransformer loading", e);
        }
    }

    class CustomVisitor extends ClassVisitor
    {
        String owner;
        public CustomVisitor(String owner ,ClassVisitor cv)
        {
            super(Opcodes.ASM4,cv);
            this.owner = owner;
        }
        @Override
        public void visitEnd() {
            MethodVisitor mv = this.visitMethod(ACC_PUBLIC, "canCreatureSpawn", "(Lnet/minecraft/entity/EnumCreatureType;Lnet/minecraft/world/IBlockAccess;III)Z", null, null);
            mv.visitCode();
            Label l0 = new Label();
            mv.visitLabel(l0);
            mv.visitLineNumber(21, l0);
            mv.visitFieldInsn(GETSTATIC, "SpawnChange/asm/SpawnChangeCorePlugin", "portalSpawn", "Z");
            Label l1 = new Label();
            mv.visitJumpInsn(IFNE, l1);
            mv.visitVarInsn(ALOAD, 2);
            mv.visitVarInsn(ILOAD, 3);
            mv.visitVarInsn(ILOAD, 4);
            mv.visitInsn(ICONST_1);
            mv.visitInsn(IADD);
            mv.visitVarInsn(ILOAD, 5);
            mv.visitMethodInsn(INVOKEINTERFACE, "net/minecraft/world/IBlockAccess", "func_147439_a", "(III)Lnet/minecraft/block/Block;");//getBlock
            mv.visitFieldInsn(GETSTATIC, "net/minecraft/init/Blocks", "field_150427_aO", "Lnet/minecraft/block/BlockPortal;");//portal
            Label l2 = new Label();
            mv.visitJumpInsn(IF_ACMPEQ, l2);
            mv.visitLabel(l1);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitInsn(ICONST_1);
            Label l3 = new Label();
            mv.visitJumpInsn(GOTO, l3);
            mv.visitLabel(l2);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitInsn(ICONST_0);
            mv.visitLabel(l3);
            mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]{Opcodes.INTEGER});
            mv.visitInsn(IRETURN);
            Label l4 = new Label();
            mv.visitLabel(l4);
            mv.visitLocalVariable("this", "Lnet/minecraft/block/BlockObsiden;", null, l0, l4, 0);
            mv.visitLocalVariable("type", "Lnet/minecraft/entity/EnumCreatureType;", null, l0, l4, 1);
            mv.visitLocalVariable("world", "Lnet/minecraft/world/IBlockAccess;", null, l0, l4, 2);
            mv.visitLocalVariable("x", "I", null, l0, l4, 3);
            mv.visitLocalVariable("y", "I", null, l0, l4, 4);
            mv.visitLocalVariable("z", "I", null, l0, l4, 5);
            mv.visitMaxs(4, 6);
            mv.visitEnd();
            super.visitEnd();
        }
    }
}
