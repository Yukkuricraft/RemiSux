all: cleanpkg cpplugin

PROD_YC_PATH=/home/minecraft/YC/YukkuriCraft
DEV_YC_PATH=/home/minecraft/YC/YC14_GD_Remi
PLUGIN_PREFIX=RemiSux

cleanpkg:
	mvn clean package

cpplugin:
	cp target/$(PLUGIN_PREFIX)-*.jar $(DEV_YC_PATH)/plugins

cppluginprod:
	cp target/$(PLUGIN_PREFIX)-*.jar $(PROD_YC_PATH)/plugins
