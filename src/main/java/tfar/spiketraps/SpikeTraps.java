package tfar.spiketraps;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.event.client.ItemTooltipCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import tfar.extratags.api.tagtypes.EnchantmentTags;

import java.util.HashSet;
import java.util.Set;

public class SpikeTraps implements ModInitializer, ClientModInitializer {

	public static final String MODID = "spiketraps";

	@Override
	public void onInitialize() {
		register(new SpikeBlock(FabricBlockSettings.of(Material.STONE).strength(6, 6).nonOpaque().build(), 5), "cobble_spike", Registry.BLOCK);
		register(new SpikeBlock(FabricBlockSettings.of(Material.METAL).strength(6, 6).nonOpaque().build(), 6), "iron_spike", Registry.BLOCK);

		register(Objects.wood_spike, "wood_spike", Registry.BLOCK);
		register(Objects.gold_spike, "gold_spike", Registry.BLOCK);
		register(Objects.diamond_spike, "diamond_spike", Registry.BLOCK);

		Item.Settings settings = new Item.Settings().group(ItemGroup.DECORATIONS);

		MOD_BLOCKS.forEach(block -> register(Registry.ITEM, Registry.BLOCK.getId(block), new SpikeBlockItem(block, settings)));

		Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(MODID, "spike_block_entity"), Objects.spike_block_entity);
	}

	private static final Set<Block> MOD_BLOCKS = new HashSet<>();

	private static <T extends ItemConvertible> T register(T obj, String name, Registry<T> registry) {
		Registry.register(registry, new Identifier(MODID, name), obj);
		if (obj instanceof Block) MOD_BLOCKS.add((Block) obj);
		return obj;
	}

	private static <T extends ItemConvertible> T register(Registry<T> registry, Identifier name, T obj) {
		Registry.register(registry,name, obj);
		return obj;
	}

	@Override
	public void onInitializeClient() {
		MOD_BLOCKS.forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout()));

		ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, list) -> {
			ListTag enchantments = EnchantedBookItem.getEnchantmentTag(itemStack);
			for(int i = 0; i < enchantments.size(); ++i) {
				CompoundTag compoundTag = enchantments.getCompound(i);
				Registry.ENCHANTMENT.getOrEmpty(Identifier.tryParse(compoundTag.getString("id"))).ifPresent((e) -> {
					EnchantmentTags.getContainer().getEntries().forEach(
									(identifier, enchantmentTag) -> {
										if (enchantmentTag.contains(e)){
											list.add(new LiteralText(identifier.toString()));
										}
									}
					);
				});
			}
		});
	}

	public static class Objects {
		public static final Block diamond_spike = new SpikeBlock(FabricBlockSettings.of(Material.METAL).strength(6, 6).nonOpaque().build(), 8);

		public static final BlockEntityType<?> spike_block_entity = BlockEntityType.Builder.create(SpikeBlockEntity::new, Objects.diamond_spike).build(null);
		public static final Block wood_spike = new SpikeBlock(FabricBlockSettings.of(Material.WOOD).strength(3, 3).nonOpaque().build(), 4);
		public static final Block gold_spike = new SpikeBlock(FabricBlockSettings.of(Material.METAL).strength(6, 6).nonOpaque().build(), 7);
		public static final Item fake_sword = register(new FakeSword(), "fake_sword", Registry.ITEM);
	}
}
