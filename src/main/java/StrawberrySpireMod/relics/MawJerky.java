package StrawberrySpireMod.relics;

import com.badlogic.gdx.graphics.*;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.relics.*;

import basemod.abstracts.*;

public class MawJerky extends CustomRelic {

    public static final String ID = "strawberrySpire:MawJerky";
    public static final Texture IMAGE_PATH = new Texture("relics/placeholder.png");
    private static final int MAX_HP_AMOUNT = 4;

    public MawJerky() {
        super(ID, IMAGE_PATH, RelicTier.COMMON, LandingSound.FLAT);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + MAX_HP_AMOUNT + DESCRIPTIONS[1];
    }

    public void onVictory() {
        if (AbstractDungeon.getCurrRoom().eliteTrigger) {
            flash();
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.player.increaseMaxHp(MAX_HP_AMOUNT, true);
        }
    }

    public AbstractRelic makeCopy() {
        return new MawJerky();
    }
}