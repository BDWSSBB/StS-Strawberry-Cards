package StrawberrySpireMod.monsters.elite;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.actions.animations.*;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.*;
import com.megacrit.cardcrawl.cards.*;
import com.megacrit.cardcrawl.cards.status.*;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.*;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.*;
import com.megacrit.cardcrawl.vfx.combat.*;

import StrawberrySpireMod.powers.*;

public class KineticPylon extends AbstractMonster {

    public static final String ID = "strawberrySpire:KineticPylon";
    private static final MonsterStrings MONSTER_STRINGS = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = MONSTER_STRINGS.NAME;
    public static final String[] MOVES = MONSTER_STRINGS.MOVES;
    private static final float HB_X = 0.0F;
    private static final float HB_Y = -5.0F;
    private static final float HB_W = 180.0F;
    private static final float HB_H = 310.0F;
    private static final int HP_MIN = 37;
    private static final int HP_MAX = 41;
    private static final int ASC_HP_MIN = 40;
    private static final int ASC_HP_MAX = 44;
    private static final byte PULSE = 1;
    private static final byte DISINTEGRATE = 2;
    private static final byte CHARGE = 3;
    private static final String PULSE_NAME = MOVES[0];
    private static final String DISINTEGRATE_NAME = MOVES[1];
    private static final String CHARGE_NAME = MOVES[2];
    private static final int INNATE_COMBUSTION_AMOUNT = 6;
    private static final int ASC2_INNATE_COMBUSTION_AMOUNT = 8;
    private static final int PULSE_DAMAGE = 4;
    private static final int PULSE_DAZE_AMOUNT = 2;
    private static final int ASC2_PULSE_DAZE_AMOUNT = 3;
    private static final int ASC_PULSE_DAMAGE = 5;
    private static final int DISINTEGRATE_DAMAGE = 7;
    private static final int ASC_DISINTEGRATE_DAMAGE = 8;
    private static final int CHARGE_STRENGTH_GAIN_AMOUNT = 1;
    private int innateCombustionAmount;
    private int pulseDamage;
    private int pulseDazeAmount;
    private int disintegrateDamage;
    private int chargeStrengthAmount;

    public KineticPylon(float x, float y) {
        super(NAME, ID, HP_MAX, HB_X, HB_Y, HB_W, HB_H, null, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(ASC_HP_MIN, ASC_HP_MAX);
        }
        else {
            setHp(HP_MIN, HP_MAX);
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.innateCombustionAmount = ASC2_INNATE_COMBUSTION_AMOUNT;
            this.pulseDamage = ASC_PULSE_DAMAGE;
            this.pulseDazeAmount = ASC2_PULSE_DAZE_AMOUNT;
            this.disintegrateDamage = ASC_DISINTEGRATE_DAMAGE;
            this.chargeStrengthAmount = CHARGE_STRENGTH_GAIN_AMOUNT;
        }
        else if (AbstractDungeon.ascensionLevel >= 3) {
            this.innateCombustionAmount = INNATE_COMBUSTION_AMOUNT;
            this.pulseDamage = ASC_PULSE_DAMAGE;
            this.pulseDazeAmount = PULSE_DAZE_AMOUNT;
            this.disintegrateDamage = ASC_DISINTEGRATE_DAMAGE;
            this.chargeStrengthAmount = CHARGE_STRENGTH_GAIN_AMOUNT;
        }
        else {
            this.innateCombustionAmount = INNATE_COMBUSTION_AMOUNT;
            this.pulseDamage = PULSE_DAMAGE;
            this.pulseDazeAmount = PULSE_DAZE_AMOUNT;
            this.disintegrateDamage = DISINTEGRATE_DAMAGE;
            this.chargeStrengthAmount = CHARGE_STRENGTH_GAIN_AMOUNT;
        }
        this.damage.add(new DamageInfo(this, pulseDamage));
        this.damage.add(new DamageInfo(this, disintegrateDamage));
        this.loadAnimation("StrawberrySpireModResources/monsters/kineticPylon/skeleton.atlas", "StrawberrySpireModResources/monsters/kineticPylon/skeleton.json", 1.0f);
        final AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTimeScale(2.0f);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("idle", "attack", 0.1f);
        this.stateData.setMix("idle", "spaz1", 0.1f);
        this.stateData.setMix("idle", "hit", 0.1f);
    }

    @Override
    public void damage(final DamageInfo info) {
        super.damage(info);
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
            this.state.setAnimation(0, "hit", false);
            this.state.addAnimation(0, "idle", true, 0.0f);
        }
    }

    @Override
    public void changeState(final String stateName) {
        switch (stateName) {
            case "ATTACK": {
                this.state.setAnimation(0, "attack", false);
                this.state.addAnimation(0, "idle", true, 0.0f);
                break;
            }
        }
    }

    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new CombustionPower(this, innateCombustionAmount), innateCombustionAmount));
    }

    public void takeTurn() {
        switch (this.nextMove) {
            case PULSE: {
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Dazed(), this.pulseDazeAmount));
                break;
            }
            case DISINTEGRATE: {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5f)); // lol this is the sentry's code
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.SKY)));
                if (Settings.FAST_MODE) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY), 0.1F));
                }
                else {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY), 0.3F));
                }
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.NONE, Settings.FAST_MODE));
                break;
            }
            case CHARGE: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, chargeStrengthAmount), chargeStrengthAmount));
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void getMove(int num) {
        if (lastMove(CHARGE)) {
            this.setMove(PULSE_NAME, PULSE, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
        }
        else if (lastMove(PULSE)) {
            this.setMove(DISINTEGRATE_NAME, DISINTEGRATE, Intent.ATTACK, this.damage.get(1).base);
        }
        else {
            this.setMove(CHARGE_NAME, CHARGE, Intent.BUFF);
        }
    }
}

