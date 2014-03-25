package SpawnChange.asm;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import cpw.mods.fml.relauncher.FMLLaunchHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.MathHelper;
import org.objectweb.asm.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by A.K. on 14/03/22.
 */
public class EntityNetherMobTransformer implements IClassTransformer, Opcodes{
    private static final Set<String> TARGET_CLASS_NAMES = new HashSet<String>();
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!FMLLaunchHandler.side().isClient() || !TARGET_CLASS_NAMES.contains(transformedName)) {return basicClass;}
        try {
            SpawnChangeCorePlugin.logger.info("Start " + transformedName + " transform");
            ClassReader classReader = new ClassReader(basicClass);
            ClassWriter classWriter = new ClassWriter(1);
            classReader.accept(new CustomVisitor(name,classWriter), 8);
            SpawnChangeCorePlugin.logger.info("Finish " + transformedName + " transform");
            return classWriter.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("failed : BlockCactusTransformer loading", e);
        }
    }
    public static boolean isValidLightLevel(EntityLiving entityLiving)
    {
        int var1 = MathHelper.floor_double(entityLiving.posX);
        int var2 = MathHelper.floor_double(entityLiving.boundingBox.minY);
        int var3 = MathHelper.floor_double(entityLiving.posZ);
        int var4 = entityLiving.worldObj.getBlockLightValue(var1, var2, var3);
        return var4 <= entityLiving.worldObj.rand.nextInt(SpawnChangeCorePlugin.netherSpawnLightValue + 1);
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
//                mv.visitCode();
                mv.visitVarInsn(ALOAD, 0);
                mv.visitMethodInsn(INVOKESTATIC, "SpawnChange/asm/EntityNetherMobTransformer", "isValidLightLevel", "(Lnet/minecraft/entity/EntityLiving;)Z");
                Label l5 = new Label();
                mv.visitJumpInsn(IFNE, l5);
                mv.visitInsn(ICONST_0);
                mv.visitInsn(IRETURN);
                mv.visitLabel(l5);
//                mv.visitLineNumber(30, l1);
//                mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);

                return mv;
            }
            return super.visitMethod(access, name, desc, signature, exceptions);
        }
    }
    static {
        TARGET_CLASS_NAMES.add("net.minecraft.entity.monster.EntityPigZombie");
        TARGET_CLASS_NAMES.add("net.minecraft.entity.monster.EntityMagmaCube");
        TARGET_CLASS_NAMES.add("net.minecraft.entity.monster.EntityGhast");
        TARGET_CLASS_NAMES.add("net.minecraft.entity.monster.EntityBlaze");
//        TARGET_CLASS_NAMES.add("net.minecraft.entity.monster.EntitySkelton");
    }
}
