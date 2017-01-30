package strategies;

import project.Board;
import project.Mark;

public class SmartStrategy implements ComputerStrategy {
	public String name = "SmartAI";
	int moveCount = 1;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int[] determineMove(Board b, Mark m) {
		int[] smartMove = new int[2];
		boolean hasWin = false;
		boolean hasLoss = false;
		boolean foundNeighbour = false;
		int direction;
		for (int k = 0; k < b.dim; k++) {
			for (int i = 0; i < b.dim; i++) {
				for (int j = 0; j < b.dim; j++) {
					Board copy = b.deepCopy();
					if (b.isField(i, j, k) && b.isEmptyField(i, j, k)) {
						copy.setField(i, j, m);
						if (copy.hasWinner()) {
							smartMove[0] = i;
							smartMove[1] = j;
							hasWin = true;
							return smartMove;
						}
					}
				}
			}
		}
		for (int k = 0; k < b.dim; k++) {
			for (int i = 0; i < b.dim; i++) {
				for (int j = 0; j < b.dim; j++) {
					Board copy = b.deepCopy();
					if (b.isField(i, j, k) && b.isEmptyField(i, j, k)) {
						assert m != null;
						copy.setField(i, j, m.other());
						if (copy.hasWinner()) {
							smartMove[0] = i;
							smartMove[1] = j;
							hasLoss = true;
							return smartMove;
						}
					}
				}
			}
		}
		if (!hasWin && !hasLoss) {
			if (moveCount == 1) {
				ComputerStrategy n = new RandomStrategy();
				smartMove = n.determineMove(b, m);
			} else {
				int[] neighbourMove = new int[3];
				for (int k = 0; k < b.dim; k++) {
					for (int i = 0; i < b.dim; i++) {
						for (int j = 0; j < b.dim; j++) {
							if (b.getField(i, j, k) == m) {
								neighbourMove[0] = i;
								neighbourMove[1] = j;
								neighbourMove[2] = k;
								if (b.hasNeighbour(neighbourMove[0], neighbourMove[1], neighbourMove[2],
										b.lastM.other())) {
									for (int l = 0; l < b.neighbourDirections.size(); l++) {
										direction = b.neighbourDirections.get(l);
										switch (direction) {
										case 1:
											if (b.isField(neighbourMove[0] + 2, neighbourMove[1], neighbourMove[2])
													&& b.isEmptyField(neighbourMove[0] + 2, neighbourMove[1],
															neighbourMove[2])) {
												neighbourMove[0] = neighbourMove[0] + 2;
												foundNeighbour = true;
											}
											break;
										case 2:
											if (b.isField(neighbourMove[0], neighbourMove[1] + 2, neighbourMove[2])
													&& b.isEmptyField(neighbourMove[0], neighbourMove[1] + 2,
															neighbourMove[2])) {
												neighbourMove[1] = neighbourMove[1] + 2;
												foundNeighbour = true;
											}
											break;
										case 3:
											if (b.isField(neighbourMove[0], neighbourMove[1], neighbourMove[2] + 2)
													&& b.isEmptyField(neighbourMove[0], neighbourMove[1],
															neighbourMove[2] + 2)) {

												neighbourMove[2] = neighbourMove[2] + 2;
												foundNeighbour = true;
											}
											break;
										case 4:
											if (b.isField(neighbourMove[0] + 2, neighbourMove[1] + 2, neighbourMove[2])
													&& b.isEmptyField(neighbourMove[0] + 2, neighbourMove[1] + 2,
															neighbourMove[2])) {
												neighbourMove[0] = neighbourMove[0] + 2;
												neighbourMove[1] = neighbourMove[1] + 2;
												foundNeighbour = true;
											}
											break;
										case 5:
											if (b.isField(neighbourMove[0], neighbourMove[1] + 2, neighbourMove[2] + 2)
													&& b.isEmptyField(neighbourMove[0] + 2, neighbourMove[1] + 2,
															neighbourMove[2])) {
												neighbourMove[1] = neighbourMove[1] + 2;
												neighbourMove[2] = neighbourMove[2] + 2;
												foundNeighbour = true;
											}
											break;
										case 6:
											if (b.isField(neighbourMove[0] + 2, neighbourMove[1], neighbourMove[2] + 2)
													&& b.isEmptyField(neighbourMove[0] + 2, neighbourMove[1],
															neighbourMove[2] + 2)) {
												neighbourMove[0] = neighbourMove[0] + 2;
												neighbourMove[2] = neighbourMove[2] + 2;
												foundNeighbour = true;
											}
											break;
										case 7:
											if (b.isField(neighbourMove[0] + 2, neighbourMove[1] + 2,
													neighbourMove[2] + 2)
													&& b.isEmptyField(neighbourMove[0] + 2, neighbourMove[1] + 2,
															neighbourMove[2] + 2)) {
												neighbourMove[0] = neighbourMove[0] + 2;
												neighbourMove[1] = neighbourMove[1] + 2;
												neighbourMove[2] = neighbourMove[2] + 2;
												foundNeighbour = true;
											}
											break;
										case 8:
											if (b.isField(neighbourMove[0] - 2, neighbourMove[1], neighbourMove[2])
													&& b.isEmptyField(neighbourMove[0] - 2, neighbourMove[1],
															neighbourMove[2])) {
												neighbourMove[0] = neighbourMove[0] - 2;
												foundNeighbour = true;
											}
											break;
										case 9:
											if (b.isField(neighbourMove[0], neighbourMove[1] - 2, neighbourMove[2])
													&& b.isEmptyField(neighbourMove[0], neighbourMove[1] - 2,
															neighbourMove[2])) {
												neighbourMove[1] = neighbourMove[1] - 2;
												foundNeighbour = true;
											}
											break;
										case 10:
											if (b.isField(neighbourMove[0], neighbourMove[1], neighbourMove[2] - 2)
													&& b.isEmptyField(neighbourMove[0], neighbourMove[1],
															neighbourMove[2] - 2)) {
												neighbourMove[2] = neighbourMove[2] - 2;
												foundNeighbour = true;
											}
											break;
										case 11:
											if (b.isField(neighbourMove[0] - 2, neighbourMove[1] - 2, neighbourMove[2])
													&& b.isEmptyField(neighbourMove[0] - 2, neighbourMove[1] - 2,
															neighbourMove[2])) {
												neighbourMove[0] = neighbourMove[0] - 2;
												neighbourMove[1] = neighbourMove[1] - 2;
												foundNeighbour = true;
											}
											break;
										case 12:
											if (b.isField(neighbourMove[0], neighbourMove[1] - 2, neighbourMove[2] - 2)
													&& b.isEmptyField(neighbourMove[0], neighbourMove[1] - 2,
															neighbourMove[2] - 2)) {
												neighbourMove[1] = neighbourMove[1] - 2;
												neighbourMove[2] = neighbourMove[2] - 2;
												foundNeighbour = true;
											}
											break;
										case 13:
											if (b.isField(neighbourMove[0] - 2, neighbourMove[1], neighbourMove[2] - 2)
													&& b.isEmptyField(neighbourMove[0] - 2, neighbourMove[1],
															neighbourMove[2] - 2)) {
												neighbourMove[0] = neighbourMove[0] - 2;
												neighbourMove[2] = neighbourMove[2] - 2;
												foundNeighbour = true;
											}
											break;
										case 14:
											if (b.isField(neighbourMove[0] - 2, neighbourMove[1] - 2,
													neighbourMove[2] - 2)
													&& b.isEmptyField(neighbourMove[0] - 2, neighbourMove[1] - 2,
															neighbourMove[2] - 2)) {
												neighbourMove[0] = neighbourMove[0] - 2;
												neighbourMove[1] = neighbourMove[1] - 2;
												neighbourMove[2] = neighbourMove[2] - 2;
												foundNeighbour = true;
											}
											break;
										case 15:
											if (b.isField(neighbourMove[0] - 2, neighbourMove[1] + 2,
													neighbourMove[2] + 2)
													&& b.isEmptyField(neighbourMove[0] - 2, neighbourMove[1] + 2,
															neighbourMove[2] + 2)) {
												neighbourMove[0] = neighbourMove[0] - 2;
												neighbourMove[1] = neighbourMove[1] + 2;
												neighbourMove[2] = neighbourMove[2] + 2;
												foundNeighbour = true;
											}
											break;
										case 16:
											if (b.isField(neighbourMove[0] + 2, neighbourMove[1] - 2,
													neighbourMove[2] + 2)
													&& b.isEmptyField(neighbourMove[0] + 2, neighbourMove[1] - 2,
															neighbourMove[2] + 2)) {
												neighbourMove[0] = neighbourMove[0] + 2;
												neighbourMove[1] = neighbourMove[1] - 2;
												neighbourMove[2] = neighbourMove[2] + 2;
												foundNeighbour = true;
											}
											break;
										case 17:
											if (b.isField(neighbourMove[0] + 2, neighbourMove[1] + 2,
													neighbourMove[2] - 2)
													&& b.isEmptyField(neighbourMove[0] + 2, neighbourMove[1] + 2,
															neighbourMove[2] - 2)) {
												neighbourMove[0] = neighbourMove[0] + 2;
												neighbourMove[1] = neighbourMove[1] + 2;
												neighbourMove[2] = neighbourMove[2] - 2;
												foundNeighbour = true;
											}
											break;
										case 18:
											if (b.isField(neighbourMove[0] - 2, neighbourMove[1] - 2,
													neighbourMove[2] + 2)
													&& b.isEmptyField(neighbourMove[0] - 2, neighbourMove[1] - 2,
															neighbourMove[2] + 2)) {
												neighbourMove[0] = neighbourMove[0] - 2;
												neighbourMove[1] = neighbourMove[1] - 2;
												neighbourMove[2] = neighbourMove[2] + 2;
												foundNeighbour = true;
											}
											break;
										case 19:
											if (b.isField(neighbourMove[0] - 2, neighbourMove[1] + 2,
													neighbourMove[2] - 2)
													&& b.isEmptyField(neighbourMove[0] - 2, neighbourMove[1] + 2,
															neighbourMove[2] - 2)) {
												neighbourMove[0] = neighbourMove[0] - 2;
												neighbourMove[1] = neighbourMove[1] + 2;
												neighbourMove[2] = neighbourMove[2] - 2;
												foundNeighbour = true;
											}
											break;
										case 20:
											if (b.isField(neighbourMove[0] + 2, neighbourMove[1] - 2,
													neighbourMove[2] - 2)
													&& b.isEmptyField(neighbourMove[0] + 2, neighbourMove[1] - 2,
															neighbourMove[2] - 2)) {
												neighbourMove[0] = neighbourMove[0] + 2;
												neighbourMove[1] = neighbourMove[1] - 2;
												neighbourMove[2] = neighbourMove[2] - 2;
												foundNeighbour = true;
											}
											break;
										}
										System.out.println(foundNeighbour);
										if (foundNeighbour) {
											smartMove[0] = neighbourMove[0];
											smartMove[1] = neighbourMove[1];
											return smartMove;
										}
									}
								}
							}
						}
					}
				}
				if (!foundNeighbour) {
					if (b.hasNeighbour(neighbourMove[0], neighbourMove[1], neighbourMove[2], Mark.EMP)) {
						for (int l = 0; l < b.neighbourDirections.size(); l++) {
							direction = b.neighbourDirections.get(l);
							switch (direction) {
							case 1:
								if (b.isField(neighbourMove[0] + 1, neighbourMove[1], neighbourMove[2])
										&& b.isEmptyField(neighbourMove[0] + 1, neighbourMove[1], neighbourMove[2])) {
									neighbourMove[0] = neighbourMove[0] + 1;
									foundNeighbour = true;
								}
								break;
							case 2:
								if (b.isField(neighbourMove[0], neighbourMove[1] + 1, neighbourMove[2])
										&& b.isEmptyField(neighbourMove[0], neighbourMove[1] + 1, neighbourMove[2])) {
									neighbourMove[1] = neighbourMove[1] + 1;
									foundNeighbour = true;
								}
								break;
							case 3:
								if (b.isField(neighbourMove[0], neighbourMove[1], neighbourMove[2] + 1)
										&& b.isEmptyField(neighbourMove[0], neighbourMove[1], neighbourMove[2] + 1)) {

									neighbourMove[2] = neighbourMove[2] + 1;
									foundNeighbour = true;
								}
								break;
							case 4:
								if (b.isField(neighbourMove[0] + 1, neighbourMove[1] + 1, neighbourMove[2]) && b
										.isEmptyField(neighbourMove[0] + 1, neighbourMove[1] + 1, neighbourMove[2])) {
									neighbourMove[0] = neighbourMove[0] + 1;
									neighbourMove[1] = neighbourMove[1] + 1;
									foundNeighbour = true;
								}
								break;
							case 5:
								if (b.isField(neighbourMove[0], neighbourMove[1] + 1, neighbourMove[2] + 1) && b
										.isEmptyField(neighbourMove[0] + 1, neighbourMove[1] + 1, neighbourMove[2])) {
									neighbourMove[1] = neighbourMove[1] + 1;
									neighbourMove[2] = neighbourMove[2] + 1;
									foundNeighbour = true;
								}
								break;
							case 6:
								if (b.isField(neighbourMove[0] + 1, neighbourMove[1], neighbourMove[2] + 1) && b
										.isEmptyField(neighbourMove[0] + 1, neighbourMove[1], neighbourMove[2] + 1)) {
									neighbourMove[0] = neighbourMove[0] + 1;
									neighbourMove[2] = neighbourMove[2] + 1;
									foundNeighbour = true;
								}
								break;
							case 7:
								if (b.isField(neighbourMove[0] + 1, neighbourMove[1] + 1, neighbourMove[2] + 1)
										&& b.isEmptyField(neighbourMove[0] + 1, neighbourMove[1] + 1,
												neighbourMove[2] + 1)) {
									neighbourMove[0] = neighbourMove[0] + 1;
									neighbourMove[1] = neighbourMove[1] + 1;
									neighbourMove[2] = neighbourMove[2] + 1;
									foundNeighbour = true;
								}
								break;
							case 8:
								if (b.isField(neighbourMove[0] - 1, neighbourMove[1], neighbourMove[2])
										&& b.isEmptyField(neighbourMove[0] - 1, neighbourMove[1], neighbourMove[2])) {
									neighbourMove[0] = neighbourMove[0] - 1;
									foundNeighbour = true;
								}
								break;
							case 9:
								if (b.isField(neighbourMove[0], neighbourMove[1] - 1, neighbourMove[2])
										&& b.isEmptyField(neighbourMove[0], neighbourMove[1] - 1, neighbourMove[2])) {
									neighbourMove[1] = neighbourMove[1] - 1;
									foundNeighbour = true;
								}
								break;
							case 10:
								if (b.isField(neighbourMove[0], neighbourMove[1], neighbourMove[2] - 1)
										&& b.isEmptyField(neighbourMove[0], neighbourMove[1], neighbourMove[2] - 1)) {
									neighbourMove[2] = neighbourMove[2] - 1;
									foundNeighbour = true;
								}
								break;
							case 11:
								if (b.isField(neighbourMove[0] - 1, neighbourMove[1] - 1, neighbourMove[2]) && b
										.isEmptyField(neighbourMove[0] - 1, neighbourMove[1] - 1, neighbourMove[2])) {
									neighbourMove[0] = neighbourMove[0] - 1;
									neighbourMove[1] = neighbourMove[1] - 1;
									foundNeighbour = true;
								}
								break;
							case 12:
								if (b.isField(neighbourMove[0], neighbourMove[1] - 1, neighbourMove[2] - 1) && b
										.isEmptyField(neighbourMove[0], neighbourMove[1] - 1, neighbourMove[2] - 1)) {
									neighbourMove[1] = neighbourMove[1] - 1;
									neighbourMove[2] = neighbourMove[2] - 1;
									foundNeighbour = true;
								}
								break;
							case 13:
								if (b.isField(neighbourMove[0] - 1, neighbourMove[1], neighbourMove[2] - 1) && b
										.isEmptyField(neighbourMove[0] - 1, neighbourMove[1], neighbourMove[2] - 1)) {
									neighbourMove[0] = neighbourMove[0] - 1;
									neighbourMove[2] = neighbourMove[2] - 1;
									foundNeighbour = true;
								}
								break;
							case 14:
								if (b.isField(neighbourMove[0] - 1, neighbourMove[1] - 1, neighbourMove[2] - 1)
										&& b.isEmptyField(neighbourMove[0] - 1, neighbourMove[1] - 1,
												neighbourMove[2] - 1)) {
									neighbourMove[0] = neighbourMove[0] - 1;
									neighbourMove[1] = neighbourMove[1] - 1;
									neighbourMove[2] = neighbourMove[2] - 1;
									foundNeighbour = true;
								}
								break;
							case 15:
								if (b.isField(neighbourMove[0] - 1, neighbourMove[1] + 1, neighbourMove[2] + 1)
										&& b.isEmptyField(neighbourMove[0] - 1, neighbourMove[1] + 1,
												neighbourMove[2] + 1)) {
									neighbourMove[0] = neighbourMove[0] - 1;
									neighbourMove[1] = neighbourMove[1] + 1;
									neighbourMove[2] = neighbourMove[2] + 1;
									foundNeighbour = true;
								}
								break;
							case 16:
								if (b.isField(neighbourMove[0] + 1, neighbourMove[1] - 1, neighbourMove[2] + 1)
										&& b.isEmptyField(neighbourMove[0] + 1, neighbourMove[1] - 1,
												neighbourMove[2] + 1)) {
									neighbourMove[0] = neighbourMove[0] + 1;
									neighbourMove[1] = neighbourMove[1] - 1;
									neighbourMove[2] = neighbourMove[2] + 1;
									foundNeighbour = true;
								}
								break;
							case 17:
								if (b.isField(neighbourMove[0] + 1, neighbourMove[1] + 1, neighbourMove[2] - 1)
										&& b.isEmptyField(neighbourMove[0] + 1, neighbourMove[1] + 1,
												neighbourMove[2] - 1)) {
									neighbourMove[0] = neighbourMove[0] + 1;
									neighbourMove[1] = neighbourMove[1] + 1;
									neighbourMove[2] = neighbourMove[2] - 1;
									foundNeighbour = true;
								}
								break;
							case 18:
								if (b.isField(neighbourMove[0] - 1, neighbourMove[1] - 1, neighbourMove[2] + 1)
										&& b.isEmptyField(neighbourMove[0] - 1, neighbourMove[1] - 1,
												neighbourMove[2] + 1)) {
									neighbourMove[0] = neighbourMove[0] - 1;
									neighbourMove[1] = neighbourMove[1] - 1;
									neighbourMove[2] = neighbourMove[2] + 1;
									foundNeighbour = true;
								}
								break;
							case 19:
								if (b.isField(neighbourMove[0] - 1, neighbourMove[1] + 1, neighbourMove[2] - 1)
										&& b.isEmptyField(neighbourMove[0] - 1, neighbourMove[1] + 1,
												neighbourMove[2] - 1)) {
									neighbourMove[0] = neighbourMove[0] - 1;
									neighbourMove[1] = neighbourMove[1] + 1;
									neighbourMove[2] = neighbourMove[2] - 1;
									foundNeighbour = true;
								}
								break;
							case 20:
								if (b.isField(neighbourMove[0] + 1, neighbourMove[1] - 1, neighbourMove[2] - 1)
										&& b.isEmptyField(neighbourMove[0] + 1, neighbourMove[1] - 1,
												neighbourMove[2] - 1)) {
									neighbourMove[0] = neighbourMove[0] + 1;
									neighbourMove[1] = neighbourMove[1] - 1;
									neighbourMove[2] = neighbourMove[2] - 1;
									foundNeighbour = true;
								}
								break;
							}
							if (foundNeighbour) {
								smartMove[0] = neighbourMove[0];
								smartMove[1] = neighbourMove[1];
								return smartMove;
							}
						}	
					}
				}
			}
		}
		moveCount++;
		return smartMove;
	}
}
