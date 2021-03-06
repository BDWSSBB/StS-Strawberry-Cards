package StrawberrySpireMod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.*;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.powers.*;

import java.util.*;

public class PackagePower extends AbstractStrawberrySpirePower implements NonStackablePower {

    public static final String POWER_ID = "strawberrySpire:Package";
    private static final PowerStrings POWER_STRINGS = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = POWER_STRINGS.NAME;
    public static final String[] DESCRIPTIONS = POWER_STRINGS.DESCRIPTIONS;
    private ArrayList<AbstractCard> cardList = new ArrayList<>();

    public PackagePower(AbstractCreature owner, ArrayList<AbstractCard> cardList) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = AbstractPower.PowerType.BUFF;
        this.owner = owner;
        for (AbstractCard c : cardList) {
            this.cardList.add(c.makeStatEquivalentCopy());
        }
        for (AbstractCard c : this.cardList) {
            c.resetAttributes();
        }
        updateDescription();
        loadRegion("heatsink");
    }

    public void updateDescription() {
        if (this.cardList.size() == 1) {
            this.description = DESCRIPTIONS[0] + FontHelper.colorString(this.cardList.get(0).name, "y") + DESCRIPTIONS[4];
        }
        else if (this.cardList.size() == 2) {
            this.description = DESCRIPTIONS[0] + FontHelper.colorString(this.cardList.get(0).name, "y") + DESCRIPTIONS[3] + FontHelper.colorString(this.cardList.get(1).name, "y") + DESCRIPTIONS[4];
        }
        else {
            String temp = "";
            for (int i = 0; i < this.cardList.size(); i++) {
                if (i == this.cardList.size() - 1) {
                    temp += DESCRIPTIONS[2] + FontHelper.colorString(this.cardList.get(i).name, "y");
                }
                else if (i == 0) {
                    temp += FontHelper.colorString(this.cardList.get(i).name, "y");
                }
                else {
                    temp += DESCRIPTIONS[1] + FontHelper.colorString(this.cardList.get(i).name, "y");
                }
            }
            this.description = DESCRIPTIONS[0] + temp + DESCRIPTIONS[4];
        }
    }

    public void atStartOfTurn() {
        flash();
        for (AbstractCard c : this.cardList) {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c));
        }
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    }
}