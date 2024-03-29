package techreborn.lib;

import reborncore.common.IModInfo;

public class ModInfo implements IModInfo {

    public static final String MOD_NAME = "TechReborn";
    public static final String MOD_ID = "techreborn";
    public static final String MOD_VERSION = "@MODVERSION@";
    public static final String MOD_DEPENDENCUIES = "required-after:Forge@[10.13.3.1374,)";
    public static final String SERVER_PROXY_CLASS = "techreborn.proxies.CommonProxy";
    public static final String CLIENT_PROXY_CLASS = "techreborn.proxies.ClientProxy";
    public static final String GUI_FACTORY_CLASS = "techreborn.config.TechRebornGUIFactory";

    @Override
    public String MOD_NAME() {
        return MOD_NAME;
    }

    @Override
    public String MOD_ID() {
        return MOD_ID;
    }

    @Override
    public String MOD_VERSION() {
        return MOD_VERSION;
    }

    @Override
    public String MOD_DEPENDENCUIES() {
        return MOD_DEPENDENCUIES;
    }

    public static final class Keys {

        public static final String CATEGORY = "keys.techreborn.category";
        public static final String CONFIG = "keys.techreborn.config";
    }
}
