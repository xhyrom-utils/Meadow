package net.satisfyu.meadow.client.recipebook;

import de.cristelknight.doapi.client.recipebook.screen.widgets.PrivateRecipeBookWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;
import net.satisfyu.meadow.Meadow;
import net.satisfyu.meadow.recipes.cooking.CookingCauldronRecipe;
import net.satisfyu.meadow.registry.RecipeRegistry;

import java.util.List;

public class CookingCauldronRecipeBook extends PrivateRecipeBookWidget {
    private static final Text TOGGLE_COOKABLE_TEXT;

    public CookingCauldronRecipeBook() {
    }

    @Override
    protected RecipeType<? extends Recipe<Inventory>> getRecipeType() {
        return RecipeRegistry.COOKING.get();
    }

    @Override
    public void insertRecipe(Recipe<?> recipe) {
        int usedInputSlots = 1;

        Meadow.LOGGER.error(recipe.getIngredients().size());

        for (Ingredient ingredient : recipe.getIngredients()) {
            int slotIndex = 0;
            for (Slot slot : screenHandler.slots) {
                ItemStack itemStack = slot.getStack();

                if (ingredient.test(itemStack) && usedInputSlots < 7) {
                    MinecraftClient.getInstance().interactionManager.clickSlot(screenHandler.syncId, slotIndex, 0, SlotActionType.PICKUP, MinecraftClient.getInstance().player);
                    MinecraftClient.getInstance().interactionManager.clickSlot(screenHandler.syncId, usedInputSlots, 0, SlotActionType.PICKUP, MinecraftClient.getInstance().player);
                    ++usedInputSlots;
                    break;
                }
                ++slotIndex;
            }
        }
    }

    @Override
    public void showGhostRecipe(Recipe<?> recipe, List<Slot> slots, DynamicRegistryManager dynamicRegistryManager) {
        if (recipe instanceof CookingCauldronRecipe potRecipe) {
            this.ghostSlots.addSlot(potRecipe.getOutput(dynamicRegistryManager), slots.get(0).x, slots.get(0).y);

            int slot = 1;
            for (Ingredient ingredient : potRecipe.getIngredients()) {
                ItemStack[] inputStacks = ingredient.getMatchingStacks();
                if (inputStacks.length == 0) continue;
                ItemStack inputStack = inputStacks[Random.create().nextBetween(0, inputStacks.length - 1)];
                this.ghostSlots.addSlot(inputStack, slots.get(slot).x, slots.get(slot++).y);
            }
        }
    }

    @Override
    protected Text getToggleCraftableButtonText() {
        return TOGGLE_COOKABLE_TEXT;
    }

    static {
        TOGGLE_COOKABLE_TEXT = Text.translatable("gui.meadow.recipebook.toggleRecipes.cookable");
    }

    @Override
    public void setFocused(boolean focused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }
}