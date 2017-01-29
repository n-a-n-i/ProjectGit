package project;

public class DummyPlayer extends Player {

	public DummyPlayer(String name, Mark mark) {
		super(name, mark);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getMoveX(Game game, Mark m) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMoveY(Game game, Mark m) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Mark getMark() {
		return mark;
	}

}
