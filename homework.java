import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class homework {

    static String moveToString(int x, int y, int tx, int ty, int type) {
        String[] x_axis = {"a", "b", "c", "d", "e", "f", "g", "h"};
        if (type == 0) {
            return "J " + x_axis[x] + (8 - y) + " " + x_axis[tx] + (8 - ty) + "\n";
        } else {
            return "E " + x_axis[x] + (8 - y) + " " + x_axis[tx] + (8 - ty) + "\n";
        }
    }

    static int conputeScore(sim sim){
        return (50 * (sim.sideNum - sim.opSideNum) + (sim.safeCount * 10) + sim.opSideNum);
    }

    static sim generateNums(sim sim, String side){
        if(side.equals("WHITE")){
            for (int j = 0; j < 8; j++) {
                for (int i = 0; i < 8; i++) {
                    switch (sim.map[i][j].type) {
                        case 'b':
                        case 'B':
                            sim.opSideNum++;
                            break;
                        case 'w':
                        case 'W':
                            sim.sideNum++;
                            if(i == 7 || i == 0){
                                sim.safeCount++;
                            }
                            break;
                    }
                }
            }
        }else{
            for (int j = 0; j < 8; j++) {
                for (int i = 0; i < 8; i++) {
                    switch (sim.map[i][j].type) {
                        case 'b':
                        case 'B':
                            sim.sideNum++;
                            if(i == 7 || i == 0){
                                sim.safeCount++;
                            }
                            break;
                        case 'w':
                        case 'W':
                            sim.opSideNum++;
                            break;
                    }
                }
            }
        }
        return sim;
    }

    static List<sim> checkMove(node node, sim sim, List<sim> sList) {
        node[][] tempMap = rollBack(sim.map);

        if (node.type == 'W' || node.type == 'B' || node.type == 'b') {
            //forward right
            if (node.x < 7 && node.y < 7) {
                if (tempMap[node.x + 1][node.y + 1].type == '.') {
                    sim newSim = new sim(tempMap, sim.score, node);
                    newSim.movement = sim.movement + moveToString(node.x, node.y, node.x + 1, node.y + 1, 1);
                    if (node.y + 1 == 7 && node.type == 'b') {
                        newSim.map[node.x][node.y].type = 'B';
                    }
                    newSim.map[node.x + 1][node.y + 1].nodeSwap(newSim.map[node.x][node.y]);
                    node = newSim.map[node.x + 1][node.y + 1];
                    newSim.node = node;
                    sList.add(newSim);
                    tempMap = rollBack(sim.map);
                    node = tempMap[node.x - 1][node.y - 1];
                }
            }
            //forward left
            if (node.x > 0 && node.y < 7) {
                if (tempMap[node.x - 1][node.y + 1].type == '.') {
                    sim newSim = new sim(tempMap, sim.score, node);
                    newSim.movement = sim.movement + moveToString(node.x, node.y, node.x - 1, node.y + 1, 1);
                    if (node.y + 1 == 7 && node.type == 'b') {
                        newSim.map[node.x][node.y].type = 'B';
                    }
                    newSim.map[node.x - 1][node.y + 1].nodeSwap(newSim.map[node.x][node.y]);
                    node = newSim.map[node.x - 1][node.y + 1];
                    newSim.node = node;
                    sList.add(newSim);
                    tempMap = rollBack(sim.map);
                    node = tempMap[node.x + 1][node.y - 1];
                }
            }
        }
        if (node.type == 'W' || node.type == 'B' || node.type == 'w') {
            //forward right
            if (node.x > 0 && node.y > 0) {
                if (tempMap[node.x - 1][node.y - 1].type == '.') {
                    sim newSim = new sim(tempMap, sim.score, node);
                    newSim.movement = sim.movement + moveToString(node.x, node.y, node.x - 1, node.y - 1, 1);
                    if (node.y - 1 == 0 && node.type == 'w') {
                        newSim.map[node.x][node.y].type = 'W';
                    }
                    newSim.map[node.x - 1][node.y - 1].nodeSwap(newSim.map[node.x][node.y]);
                    node = newSim.map[node.x - 1][node.y - 1];
                    newSim.node = node;
                    sList.add(newSim);
                    tempMap = rollBack(sim.map);
                    node = tempMap[node.x + 1][node.y + 1];
                }
            }
            //forward left
            if (node.x < 7 && node.y > 0) {
                if (tempMap[node.x + 1][node.y - 1].type == '.') {
                    sim newSim = new sim(tempMap, sim.score, node);
                    newSim.movement = sim.movement + moveToString(node.x, node.y, node.x + 1, node.y - 1, 1);
                    if (node.y - 1 == 0 && node.type == 'w') {
                        newSim.map[node.x][node.y].type = 'W';
                    }
                    newSim.map[node.x + 1][node.y - 1].nodeSwap(newSim.map[node.x][node.y]);
                    node = newSim.map[node.x + 1][node.y - 1];
                    newSim.node = node;
                    sList.add(newSim);
                }
            }
        }
        return sList;
    }

    static List<sim> checkJump(node node, sim sim, List<sim> sList) {
        node[][] tempMap = rollBack(sim.map);
        if (node.type == 'W' || node.type == 'b' || node.type == 'B') {
            if (node.x > 0 && node.y < 7) {
                //check for jump movement: left backward
                if (tempMap[node.x - 1][node.y + 1].type != Character.toLowerCase(node.type) && tempMap[node.x - 1][node.y + 1].type != Character.toUpperCase(node.type) && tempMap[node.x - 1][node.y + 1].type != '.') {
                    if (node.x > 1 && node.y < 6) {
                        if (tempMap[node.x - 2][node.y + 2].type == '.') {
                            boolean changed = false;
                            sim newSim = new sim(tempMap, sim.score, node);
                            newSim.movement = sim.movement + moveToString(node.x, node.y, node.x - 2, node.y + 2, 0);
                            if (node.y + 2 == 7 && node.type == 'b') {
                                newSim.map[node.x][node.y].type = 'B';
                                changed = true;
                            }
                            newSim.map[node.x - 1][node.y + 1].nodeClear();
                            newSim.map[node.x - 2][node.y + 2].nodeSwap(newSim.map[node.x][node.y]);
                            node = newSim.map[node.x - 2][node.y + 2];
                            newSim.node = node;
                            if (sList.contains(sim)) {
                                sList.remove(sim);
                            }
                            sList.add(newSim);
                            if (!changed) {
                                checkJump(node, newSim, sList);
                            }
                            tempMap = rollBack(sim.map);
                            node = tempMap[node.x + 2][node.y - 2];
                        }
                    }
                }
            }
            if (node.x < 7 && node.y < 7) {
                //check for jump movement: right backward
                if (tempMap[node.x + 1][node.y + 1].type != Character.toLowerCase(node.type) && tempMap[node.x + 1][node.y + 1].type != Character.toUpperCase(node.type) && tempMap[node.x + 1][node.y + 1].type != '.') {
                    if (node.x < 6 && node.y < 6) {
                        if (tempMap[node.x + 2][node.y + 2].type == '.') {
                            boolean changed = false;
                            sim newSim = new sim(tempMap, sim.score, node);
                            newSim.movement = sim.movement + moveToString(node.x, node.y, node.x + 2, node.y + 2, 0);
                            if (node.y + 2 == 7 && node.type == 'b') {
                                newSim.map[node.x][node.y].type = 'B';
                                changed = true;
                            }
                            newSim.map[node.x + 1][node.y + 1].nodeClear();
                            newSim.map[node.x + 2][node.y + 2].nodeSwap(newSim.map[node.x][node.y]);
                            node = newSim.map[node.x + 2][node.y + 2];
                            newSim.node = node;
                            if (sList.contains(sim)) {
                                sList.remove(sim);
                            }
                            sList.add(newSim);
                            if (!changed) {
                                checkJump(node, newSim, sList);
                            }
                            tempMap = rollBack(sim.map);
                            node = tempMap[node.x - 2][node.y - 2];
                        }
                    }
                }
            }
        }
        if (node.type == 'W' || node.type == 'B' || node.type == 'w') {
            if (node.x < 7 && node.y > 0) {
                //check for jump movement: forward left
                if (tempMap[node.x + 1][node.y - 1].type != Character.toLowerCase(node.type) && tempMap[node.x + 1][node.y - 1].type != Character.toUpperCase(node.type) && tempMap[node.x + 1][node.y - 1].type != '.') {
                    if (node.x < 6 && node.y > 1) {
                        if (tempMap[node.x + 2][node.y - 2].type == '.') {
                            boolean changed = false;
                            sim newSim = new sim(tempMap, sim.score, node);
                            newSim.movement = sim.movement + moveToString(node.x, node.y, node.x + 2, node.y - 2, 0);
                            if (node.y - 2 == 0 && node.type == 'w') {
                                newSim.map[node.x][node.y].type = 'W';
                                changed = true;
                            }
                            newSim.map[node.x + 1][node.y - 1].nodeClear();
                            newSim.map[node.x + 2][node.y - 2].nodeSwap(newSim.map[node.x][node.y]);
                            node = newSim.map[node.x + 2][node.y - 2];
                            newSim.node = node;
                            if (sList.contains(sim)) {
                                sList.remove(sim);
                            }
                            sList.add(newSim);
                            if (!changed) {
                                checkJump(node, newSim, sList);
                            }
                            tempMap = rollBack(sim.map);
                            node = tempMap[node.x - 2][node.y + 2];
                        }
                    }
                }
            }
            if (node.x > 0 && node.y > 0) {
                //check for jump movement
                if (tempMap[node.x - 1][node.y - 1].type != Character.toLowerCase(node.type) && tempMap[node.x - 1][node.y - 1].type != Character.toUpperCase(node.type) && tempMap[node.x - 1][node.y - 1].type != '.') {
                    if (node.x > 1 && node.y > 1) {
                        if (tempMap[node.x - 2][node.y - 2].type == '.') {
                            boolean changed = false;
                            sim newSim = new sim(tempMap, sim.score, node);
                            newSim.movement = sim.movement + moveToString(node.x, node.y, node.x - 2, node.y - 2, 0);
                            if (node.y - 2 == 0 && node.type == 'w') {
                                newSim.map[node.x][node.y].type = 'W';
                                changed = true;
                            }
                            newSim.map[node.x - 1][node.y - 1].nodeClear();
                            newSim.map[node.x - 2][node.y - 2].nodeSwap(newSim.map[node.x][node.y]);
                            node = newSim.map[node.x - 2][node.y - 2];
                            newSim.node = node;
                            if (sList.contains(sim)) {
                                sList.remove(sim);
                            }
                            sList.add(newSim);
                            if (!changed) {
                                checkJump(node, newSim, sList);
                            }
                        }
                    }
                }
            }
        }
        return sList;
    }

    static List<sim> generateTree(String side, node[][] map, sim action) {
        List<node> bList = new ArrayList<>();
        List<node> wList = new ArrayList<>();
        node[][] tempMap;
        node[][] tempMap2 = rollBack(map);
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                switch (action.map[i][j].type) {
                    case 'b':
                    case 'B':
                        bList.add(map[i][j]);
                        break;
                    case 'w':
                    case 'W':
                        wList.add(map[i][j]);
                        break;
                }
            }
        }
        List<sim> sList = new ArrayList<>();
        List<sim> mList = new ArrayList<>();
        if (side.equals("WHITE")) {
            for (homework.node node : wList) {
                tempMap = rollBack(tempMap2);
                sim sim = new sim(tempMap, 0, node);
                sList = checkJump(node, sim, sList);
                mList.addAll(sList);
                sList.clear();
            }
            if (!mList.isEmpty()) {
                action.children = mList;
            } else {
                for (homework.node node : wList) {
                    tempMap = rollBack(tempMap2);
                    sim sim = new sim(tempMap, 0, node);
                    sList = checkMove(node, sim, sList);
                    mList.addAll(sList);
                    sList.clear();
                }
            }
        } else {
            for (homework.node node : bList) {
                tempMap = rollBack(tempMap2);
                sim sim = new sim(tempMap, 0, node);
                sList = checkJump(node, sim, sList);
                mList.addAll(sList);
                sList.clear();
            }
            if (!mList.isEmpty()) {
                action.children = mList;
            } else {
                for (homework.node node : bList) {
                    tempMap = rollBack(tempMap2);
                    sim sim = new sim(tempMap, 0, node);
                    sList = checkMove(node, sim, sList);
                    mList.addAll(sList);
                    sList.clear();
                }
            }
        }
        return mList;
    }

    static node[][] rollBack(node[][] map) {
        node[][] tempMap = new node[8][8];
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                tempMap[i][j] = new node(i, j, '.');
                tempMap[i][j].nodeCopy(map[i][j]);
            }
        }
        return tempMap;
    }

    static sim getMin(List<sim> sims, int smallest) {
        int pos = 0;
        int min = sims.get(0).score;
        for (int i = 0; i < sims.size(); i++) {
            if (sims.get(i).score >= smallest) {
                if (sims.get(i).score < min) {
                    min = sims.get(i).score;
                    pos = i;
                }
            } else {
                return sims.get(i);
            }
        }
        return sims.get(pos);
    }

    static sim getMax(List<sim> sims, int largest) {
        int pos = 0;
        int max = sims.get(0).score;
        for (int i = 0; i < sims.size(); i++) {
            if (sims.get(i).score <= largest) {
                if (sims.get(i).score > max) {
                    max = sims.get(i).score;
                    pos = i;
                }
            } else {
                return sims.get(i);
            }
        }
        return sims.get(pos);
    }

    //build in depth 3
    static sim quickestMinimax(sim action, String side, String opSide, List<sim> sims) {
        int sim3Min = Integer.MAX_VALUE;
        int sim2Max = Integer.MIN_VALUE;
        int sim1Min = Integer.MAX_VALUE;
        action.children = new ArrayList<>(sims);
        List<sim> simList = new ArrayList<>();
        for (homework.sim sim1 : sims) {
            simList.clear();
            simList = generateTree(opSide, sim1.map, sim1);
            sim1.children = new ArrayList<>(simList);
            for (homework.sim sim2 : sim1.children) {
                simList.clear();
                simList = generateTree(side, sim2.map, sim2);
                sim2.children = new ArrayList<>(simList);
                for (homework.sim sim3 : sim2.children) {
                    simList.clear();
                    simList = generateTree(opSide, sim3.map, sim3);
                    sim3.children = new ArrayList<>(simList);
                    if (!sim3.children.isEmpty()) {
                        sim3 = generateNums(sim3, side);
                        sim3.score = conputeScore(sim3);
                    } else {
                        sim3.score = 900;
                    }
                }
                if (!sim2.children.isEmpty()) {
                    sim largest2;
                    if (sim2 == sim1.children.get(0)) {
                        largest2 = getMax(sim2.children, sim2Max);
                        sim2Max = largest2.score;
                    } else {
                        largest2 = getMax(sim2.children, sim2Max);
                        if (largest2.score > sim2Max) {
                            sim2Max = largest2.score;
                        }
                    }
                    sim2.score = largest2.score;
                    sim2.children = null;
                } else {
                    sim2.score = -1000;
                }
            }
            if (!sim1.children.isEmpty()) {
                sim smallest1;
                if (sim1 == sims.get(0)) {
                    smallest1 = getMin(sim1.children, sim1Min);
                    sim1Min = smallest1.score;
                } else {
                    smallest1 = getMin(sim1.children, sim1Min);
                    if (smallest1.score < sim1Min) {
                        sim3Min = smallest1.score;
                    }
                }
                sim1.score = smallest1.score;
                sim1.children = null;
            } else {
                sim1.score = 1000;
            }
        }
        if (!sims.isEmpty()) {
            sim largest = getMax(sims, Integer.MAX_VALUE);
            action = largest;
            action.children = null;
        }
        return action;
    }

    //build in depth 5
    static sim quickMinimax(sim action, String side, String opSide, List<sim> sims) {
        int sim5Min = Integer.MAX_VALUE;
        int sim4Max = Integer.MIN_VALUE;
        int sim3Min = Integer.MAX_VALUE;
        int sim2Max = Integer.MIN_VALUE;
        int sim1Min = Integer.MAX_VALUE;
        action.children = new ArrayList<>(sims);
        List<sim> simList = new ArrayList<>();
        for (homework.sim sim1 : sims) {
            simList.clear();
            simList = generateTree(opSide, sim1.map, sim1);
            sim1.children = new ArrayList<>(simList);
            for (homework.sim sim2 : sim1.children) {
                simList.clear();
                simList = generateTree(side, sim2.map, sim2);
                sim2.children = new ArrayList<>(simList);
                for (homework.sim sim3 : sim2.children) {
                    simList.clear();
                    simList = generateTree(opSide, sim3.map, sim3);
                    sim3.children = new ArrayList<>(simList);
                    for (homework.sim sim4 : sim3.children) {
                        simList.clear();
                        simList = generateTree(side, sim4.map, sim4);
                        sim4.children = new ArrayList<>(simList);
                        for (homework.sim sim5 : sim4.children) {
                            simList.clear();
                            simList = generateTree(opSide, sim5.map, sim5);
                            sim5.children = new ArrayList<>(simList);
                            if (!sim5.children.isEmpty()) {
                                sim5 = generateNums(sim5, side);
                                sim5.score = conputeScore(sim5);
                            } else {
                                sim5.score = 800;
                            }
                        }
                        if (!sim4.children.isEmpty()) {
                            sim largest4;
                            if (sim4 == sim3.children.get(0)) {
                                largest4 = getMax(sim4.children, sim4Max);
                                sim4Max = largest4.score;
                            } else {
                                largest4 = getMax(sim4.children, sim4Max);
                                if (largest4.score > sim4Max) {
                                    sim4Max = largest4.score;
                                }
                            }
                            sim4.score = largest4.score;
                            sim4.children = null;
                        } else {
                            sim4.score = -900;
                        }
                    }
                    if (!sim3.children.isEmpty()) {
                        sim smallest3;
                        if (sim3 == sim2.children.get(0)) {
                            smallest3 = getMin(sim3.children, sim3Min);
                            sim3Min = smallest3.score;
                        } else {
                            smallest3 = getMin(sim3.children, sim3Min);
                            if (smallest3.score < sim3Min) {
                                sim3Min = smallest3.score;
                            }
                        }
                        sim3.score = smallest3.score;
                        sim3.children = null;
                    } else {
                        sim3.score = 900;
                    }
                }
                if (!sim2.children.isEmpty()) {
                    sim largest2;
                    if (sim2 == sim1.children.get(0)) {
                        largest2 = getMax(sim2.children, sim2Max);
                        sim2Max = largest2.score;
                    } else {
                        largest2 = getMax(sim2.children, sim2Max);
                        if (largest2.score > sim2Max) {
                            sim2Max = largest2.score;
                        }
                    }
                    sim2.score = largest2.score;
                    sim2.children = null;
                } else {
                    sim2.score = -1000;
                }
            }
            if (!sim1.children.isEmpty()) {
                sim smallest1;
                if (sim1 == sims.get(0)) {
                    smallest1 = getMin(sim1.children, sim1Min);
                    sim1Min = smallest1.score;
                } else {
                    smallest1 = getMin(sim1.children, sim1Min);
                    if (smallest1.score < sim1Min) {
                        sim1Min = smallest1.score;
                    }
                }
                sim1.score = smallest1.score;
                sim1.children = null;
            } else {
                sim1.score = 1000;
            }
        }
        if (!sims.isEmpty()) {
            sim largest = getMax(sims, Integer.MAX_VALUE);
            action = largest;
            action.children = null;
        }
        return action;
    }

    //build in depth 6
    static sim slowMinimax(sim action, String side, String opSide, List<sim> sims) {
        int sim6Max = Integer.MIN_VALUE;
        int sim5Min = Integer.MAX_VALUE;
        int sim4Max = Integer.MIN_VALUE;
        int sim3Min = Integer.MAX_VALUE;
        int sim2Max = Integer.MIN_VALUE;
        int sim1Min = Integer.MAX_VALUE;
        action.children = new ArrayList<>(sims);
        List<sim> simList = new ArrayList<>();
        for (homework.sim sim : sims) {
            simList.clear();
            simList = generateTree(opSide, sim.map, sim);
            sim.children = new ArrayList<>(simList);
            for (homework.sim sim2 : sim.children) {
                simList.clear();
                simList = generateTree(side, sim2.map, sim2);
                sim2.children = new ArrayList<>(simList);
                for (homework.sim sim3 : sim2.children) {
                    simList.clear();
                    simList = generateTree(opSide, sim3.map, sim3);
                    sim3.children = new ArrayList<>(simList);
                    for (homework.sim sim4 : sim3.children) {
                        simList.clear();
                        simList = generateTree(side, sim4.map, sim4);
                        sim4.children = new ArrayList<>(simList);
                        for (homework.sim sim5 : sim4.children) {
                            simList.clear();
                            simList = generateTree(opSide, sim5.map, sim5);
                            sim5.children = new ArrayList<>(simList);
                            for (homework.sim sim6 : sim5.children) {
                                simList.clear();
                                simList = generateTree(side, sim6.map, sim6);
                                sim6.children = new ArrayList<>(simList);
                                if (!sim6.children.isEmpty()) {
                                    sim6 = generateNums(sim6, side);
                                    sim6.score = conputeScore(sim6);
                                } else {
                                    sim6.score = -800;
                                }
                            }
                            if (!sim5.children.isEmpty()) {
                                sim smallest5;
                                if (sim5 == sim4.children.get(0)) {
                                    smallest5 = getMin(sim5.children, sim5Min);
                                    sim5Min = smallest5.score;
                                } else {
                                    smallest5 = getMin(sim5.children, sim5Min);
                                    if (smallest5.score < sim5Min) {
                                        sim5Min = smallest5.score;
                                    }
                                }
                                sim5.score = smallest5.score;
                                sim5.children = null;
                            } else {
                                sim5.score = 800;
                            }
                        }
                        if (!sim4.children.isEmpty()) {
                            sim largest4;
                            if (sim4 == sim3.children.get(0)) {
                                largest4 = getMax(sim4.children, sim4Max);
                                sim4Max = largest4.score;
                            } else {
                                largest4 = getMax(sim4.children, sim4Max);
                                if (largest4.score > sim4Max) {
                                    sim4Max = largest4.score;
                                }
                            }
                            sim4.score = largest4.score;
                            sim4.children = null;
                        } else {
                            sim4.score = -900;
                        }
                    }
                    if (!sim3.children.isEmpty()) {
                        sim smallest3;
                        if (sim3 == sim2.children.get(0)) {
                            smallest3 = getMin(sim3.children, sim3Min);
                            sim3Min = smallest3.score;
                        } else {
                            smallest3 = getMin(sim3.children, sim3Min);
                            if (smallest3.score < sim3Min) {
                                sim3Min = smallest3.score;
                            }
                        }
                        sim3.score = smallest3.score;
                        sim3.children = null;
                    } else {
                        sim3.score = 900;
                    }
                }
                if (!sim2.children.isEmpty()) {
                    sim largest2;
                    if (sim2 == sim.children.get(0)) {
                        largest2 = getMax(sim2.children, sim2Max);
                        sim2Max = largest2.score;
                    } else {
                        largest2 = getMax(sim2.children, sim2Max);
                        if (largest2.score > sim2Max) {
                            sim2Max = largest2.score;
                        }
                    }
                    sim2.score = largest2.score;
                    sim2.children = null;
                } else {
                    sim2.score = -1000;
                }
            }
            if (!sim.children.isEmpty()) {
                sim smallest1;
                if (sim == sims.get(0)) {
                    smallest1 = getMin(sim.children, sim1Min);
                    sim3Min = smallest1.score;
                } else {
                    smallest1 = getMin(sim.children, sim1Min);
                    if (smallest1.score < sim1Min) {
                        sim1Min = smallest1.score;
                    }
                }
                sim.score = smallest1.score;
                sim.children = null;
            } else {
                sim.score = 1000;
            }
        }
        if (!sims.isEmpty()) {
            sim largest = getMax(sims, Integer.MAX_VALUE);
            action = largest;
            action.children = null;
        }
        return action;
    }


    public static void main(String[] args) throws IOException {
        //declare essential utilities
        File input = new File("input.txt");
        File plydata = new File("playdata.txt");
        FileWriter myWriter2 = new FileWriter("map.txt");
        int round = 1;
        if (plydata.exists()) {
            Scanner sc2 = new Scanner(plydata);
            round = Integer.parseInt(sc2.nextLine());
            FileWriter myWriter3 = new FileWriter("playdata.txt");
            myWriter3.write(round + 1 + "");
            myWriter3.close();
        } else {
            FileWriter myWriter3 = new FileWriter("playdata.txt");
            myWriter3.write("1");
            myWriter3.close();
        }
        FileWriter myWriter = new FileWriter("output.txt");
        Scanner sc = new Scanner(input);
        String type = sc.nextLine();
        String side = sc.nextLine();
        String opSide = "";
        double totalTime = sc.nextDouble();
        node[][] map = new node[8][8];
        List<sim> sims;

        switch (side) {
            case "WHITE":
                opSide = "BLACK";
                break;
            case "BLACK":
                opSide = "WHITE";
                break;
        }

        for (int j = 0; j < 8; j++) {
            String nl = sc.next();
            for (int i = 0; i < 8; i++) {
                map[i][j] = new node(i, j, nl.charAt(i));
            }
        }
        //generate the first simulation
        sim action = new sim(map, 0, null);
        sims = generateTree(side, map, action);
        //if it's single mode, output the first movement as operation
        if (type.equals("SINGLE") || totalTime < 0.2) {
            myWriter.write(sims.get(0).movement);
            myWriter2.write("GAME\n" + side + "\n300\n");
            for (int j = 0; j < 8; j++) {
                for (int i = 0; i < 8; i++) {
                    myWriter2.write(sims.get(0).map[i][j].type);
                }
                myWriter2.write("\n");
            }
        } else {
            //if there is only one move, take it without minimax
            if (sims.size() == 1) {
                myWriter.write(sims.get(0).movement);
                myWriter2.write("GAME\n" + side + "\n300\n");
                for (int j = 0; j < 8; j++) {
                    for (int i = 0; i < 8; i++) {
                        myWriter2.write(sims.get(0).map[i][j].type);
                    }
                    myWriter2.write("\n");
                }
            } else {
                if (totalTime < 100 || round < 6) {
                    action = quickMinimax(action, side, opSide, sims);
                    myWriter.write(action.movement);
                } else if (totalTime < 5) {
                    action = quickestMinimax(action, side, opSide, sims);
                    myWriter.write(action.movement);
                } else {
                    action = slowMinimax(action, side, opSide, sims);
                    myWriter.write(action.movement);
                }
                myWriter2.write("GAME\n" + side + "\n300\n");
                for (int j = 0; j < 8; j++) {
                    for (int i = 0; i < 8; i++) {
                        myWriter2.write(action.map[i][j].type);
                    }
                    myWriter2.write("\n");
                }
            }
        }
        myWriter2.close();
        myWriter.close();
    }

    //declare the data structure needed
    static class node {
        int x;
        int y;
        char type;

        public node(int x, int y, char type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }

        public void nodeClear() {
            type = '.';
        }

        public void nodeCopy(node node) {
            this.type = node.type;
        }

        public void nodeSwap(node from) {
            char tempType = from.type;
            from.type = this.type;
            this.type = tempType;
        }
    }

    static class sim {
        node node;
        node[][] map;
        int score;
        int sideNum;
        int opSideNum;
        int safeCount;
        List<sim> children;
        List<sim> elementsIndex;
        String movement = "";

        public sim(node[][] map, int score, node node) {
            this.map = map;
            this.score = score;
            this.children = new LinkedList<>();
            this.elementsIndex = new LinkedList<>();
            this.elementsIndex.add(this);
            this.node = node;
        }

    }
}