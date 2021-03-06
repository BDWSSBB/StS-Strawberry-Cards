package StrawberrySpireMod.actions.unique;

import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.powers.*;

public class PowerStompAction extends AbstractGameAction {

    private AbstractCreature target;
    private AbstractPlayer player;
    private int strengthDownAmount;

    public PowerStompAction(AbstractCreature monster, int magicNumber) {
        this.target = monster;
        this.player = AbstractDungeon.player;
        this.strengthDownAmount = magicNumber;
    }

    public void update() {
        if (this.target != null && this.target.hasPower(VulnerablePower.POWER_ID)) {
            AbstractDungeon.actionManager.addToTop(new ApplyDebuffAndInverseAction(this.target, this.player, new StrengthPower(this.target, -this.strengthDownAmount), -this.strengthDownAmount, new GainStrengthPower(this.target, this.strengthDownAmount), this.strengthDownAmount));
        }
        this.isDone = true;
    }
}
