package univ.lmd.result;

public class Diving implements HeroState {

    public HeroState up() {
        return this;
    }

    public HeroState down() {
        return this;
    }

    public HeroState release() {
        return this;
    }

    public HeroState next() {
        return new Standing();
    }
}