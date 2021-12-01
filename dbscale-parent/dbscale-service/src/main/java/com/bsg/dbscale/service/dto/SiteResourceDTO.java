package com.bsg.dbscale.service.dto;

import java.io.Serializable;

public class SiteResourceDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private ServGroup servGroup;
    private Unit unit;
    private Resource resource;

    public ServGroup getServGroup() {
        return servGroup;
    }

    public void setServGroup(ServGroup servGroup) {
        this.servGroup = servGroup;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "SiteResourceDTO [servGroup=" + servGroup + ", unit=" + unit + ", resource=" + resource + "]";
    }

    public class ServGroup {
        private State mysql;
        private State cmha;
        private State redis;

        public State getMysql() {
            return mysql;
        }

        public void setMysql(State mysql) {
            this.mysql = mysql;
        }

        public State getCmha() {
            return cmha;
        }

        public void setCmha(State cmha) {
            this.cmha = cmha;
        }

        public State getRedis() {
            return redis;
        }

        public void setRedis(State redis) {
            this.redis = redis;
        }

        @Override
        public String toString() {
            return "ServGroup [mysql=" + mysql + ", cmha=" + cmha + ", redis=" + redis + "]";
        }

    }

    public class Unit {
        private int passingCnt;
        private int criticalCnt;
        private int unknownCnt;

        public int getPassingCnt() {
            return passingCnt;
        }

        public void setPassingCnt(int passingCnt) {
            this.passingCnt = passingCnt;
        }

        public int getCriticalCnt() {
            return criticalCnt;
        }

        public void setCriticalCnt(int criticalCnt) {
            this.criticalCnt = criticalCnt;
        }

        public int getUnknownCnt() {
            return unknownCnt;
        }

        public void setUnknownCnt(int unknownCnt) {
            this.unknownCnt = unknownCnt;
        }

        @Override
        public String toString() {
            return "Unit [passingCnt=" + passingCnt + ", criticalCnt=" + criticalCnt + ", unknownCnt=" + unknownCnt
                    + "]";
        }

    }

    public class Resource {
        private HostState host;
        private StatisticsDTO<Integer> network;
        private StatisticsDTO<Double> cpu;
        private StatisticsDTO<Double> mem;
        private StatisticsDTO<Double> hdd;
        private StatisticsDTO<Double> ssd;
        private StatisticsDTO<Integer> unit;

        public HostState getHost() {
            return host;
        }

        public void setHost(HostState host) {
            this.host = host;
        }

        public StatisticsDTO<Integer> getNetwork() {
            return network;
        }

        public void setNetwork(StatisticsDTO<Integer> network) {
            this.network = network;
        }

        public StatisticsDTO<Double> getCpu() {
            return cpu;
        }

        public void setCpu(StatisticsDTO<Double> cpu) {
            this.cpu = cpu;
        }

        public StatisticsDTO<Double> getMem() {
            return mem;
        }

        public void setMem(StatisticsDTO<Double> mem) {
            this.mem = mem;
        }

        public StatisticsDTO<Double> getHdd() {
            return hdd;
        }

        public void setHdd(StatisticsDTO<Double> hdd) {
            this.hdd = hdd;
        }

        public StatisticsDTO<Double> getSsd() {
            return ssd;
        }

        public void setSsd(StatisticsDTO<Double> ssd) {
            this.ssd = ssd;
        }

        public StatisticsDTO<Integer> getUnit() {
            return unit;
        }

        public void setUnit(StatisticsDTO<Integer> unit) {
            this.unit = unit;
        }

        @Override
        public String toString() {
            return "Resource [host=" + host + ", network=" + network + ", cpu=" + cpu + ", mem=" + mem + ", hdd=" + hdd
                    + ", ssd=" + ssd + ", unit=" + unit + "]";
        }

        public class HostState extends State {
            private int distributableCnt;
            private int undistributableCnt;

            public int getDistributableCnt() {
                return distributableCnt;
            }

            public void setDistributableCnt(int distributableCnt) {
                this.distributableCnt = distributableCnt;
            }

            public int getUndistributableCnt() {
                return undistributableCnt;
            }

            public void setUndistributableCnt(int undistributableCnt) {
                this.undistributableCnt = undistributableCnt;
            }

            @Override
            public String toString() {
                return super.toString() + "HostState [distributableCnt=" + distributableCnt + ", undistributableCnt="
                        + undistributableCnt + "]";
            }

        }

    }

    public class State {
        private int passingCnt;
        private int warningCnt;
        private int criticalCnt;

        public int getPassingCnt() {
            return passingCnt;
        }

        public void setPassingCnt(int passingCnt) {
            this.passingCnt = passingCnt;
        }

        public int getWarningCnt() {
            return warningCnt;
        }

        public void setWarningCnt(int warningCnt) {
            this.warningCnt = warningCnt;
        }

        public int getCriticalCnt() {
            return criticalCnt;
        }

        public void setCriticalCnt(int criticalCnt) {
            this.criticalCnt = criticalCnt;
        }

        @Override
        public String toString() {
            return "State [passingCnt=" + passingCnt + ", warningCnt=" + warningCnt + ", criticalCnt=" + criticalCnt
                    + "]";
        }

    }

}
