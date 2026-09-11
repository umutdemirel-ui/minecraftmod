package com.ultimatelife.common.config;

import com.ultimatelife.UltimateLife;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Forge config for Ultimate Life.
 *
 * <p>The spec is built once, at class-load time, and handed to
 * {@code FMLJavaModLoadingContext#registerConfig} during mod construction. Values are read
 * through the {@link Common} holder rather than cached into statics, which means config
 * reloads ({@code /forge config reload}) take effect without a restart.
 *
 * <h2>Adding a new section</h2>
 * Each future system gets its own nested holder that receives the shared builder, e.g.
 * <pre>{@code
 * public static final class Common {
 *     private final General general;
 *     private final Furniture furniture;   // new
 *
 *     private Common(ForgeConfigSpec.Builder builder) {
 *         this.general = new General(builder);
 *         this.furniture = new Furniture(builder);
 *     }
 * }
 * }</pre>
 * Keep one holder per system so that settings never bleed across system boundaries.
 */
public final class UltimateLifeConfig {

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<Common, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = pair.getLeft();
        COMMON_SPEC = pair.getRight();
    }

    private UltimateLifeConfig() {
    }

    /** Values shared by both physical sides. */
    public static final class Common {

        private final General general;

        private Common(final ForgeConfigSpec.Builder builder) {
            builder.push("general");
            this.general = new General(builder);
            builder.pop();
            // Future systems append their own push/pop block here.
        }

        public General general() {
            return this.general;
        }
    }

    /** Cross-cutting, mod-wide switches. */
    public static final class General {

        private final ForgeConfigSpec.BooleanValue startupLogging;

        private General(final ForgeConfigSpec.Builder builder) {
            this.startupLogging = builder
                .comment("Log a short summary line while " + UltimateLife.MOD_NAME + " boots.")
                .define("startupLogging", true);
        }

        public ForgeConfigSpec.BooleanValue startupLogging() {
            return this.startupLogging;
        }
    }
}
