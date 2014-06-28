package SpawnChange.asm;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;

/**
 * Created by A.K. on 14/03/22.
 */
public class EntitySlimeTransformer implements IClassTransformer, Opcodes{
    private static final String TARGET_CLASS_NAME = "net.minecraft.entity.monster.EntitySlime";
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
            throw new RuntimeException("failed : EntitySlimeTransformer loading", e);
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

        static final String targetMethodName = "func_70601_bi";//getCanSpawnHere
        static final String targetMethoddesc = "()Z";

        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
        {

            if (targetMethodName.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(owner, name, desc))
                    && targetMethoddesc.equals(FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(desc)))
            {
                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

                mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, "net/minecraft/entity/monster/EntitySlime", "field_70163_u", "D");//posY
                mv.visitFieldInsn(GETSTATIC, "SpawnChange/asm/SpawnChangeCorePlugin", "SlimeSpawnHeight", "I");
                mv.visitInsn(I2D);
                mv.visitInsn(DCMPG);
                Label l1 = new Label();
                mv.visitJumpInsn(IFGE, l1);
                mv.visitInsn(ICONST_0);
                mv.visitInsn(IRETURN);
                mv.visitLabel(l1);
//                mv.visitLineNumber(24, l1);
                mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);

                return mv;
            }
            return super.visitMethod(access, name, desc, signature, exceptions);
        }
    }
}
