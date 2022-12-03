package univ.lmd.result;

public class Jumping implements HeroState {

    public HeroState up() {
        return this;
    }

    public HeroState down() {
        return new Diving();
    }

    public HeroState release() {
        return this;
    }

    public HeroState next() {
        return new Standing();
    }
}