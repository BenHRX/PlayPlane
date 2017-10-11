package ben.home.cn.share;

/**
 * Created by benhuang on 17-10-12.
 */

public interface ElementStatus {
    int TARGETDESTROYED = -1;            // static final is a hidden key word in interface
    int TARGETALIVE = 1;
    int getHealPower();
    void setHealPower(int heal);
    int getAttackValue();
    void setAttackValue(int attackValue);
    int checkDestroyStatus(int outterDamage);  // Use default key word will request a upper API.
}
