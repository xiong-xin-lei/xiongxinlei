package com.bsg.dbscale.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskDetailDTO extends TaskDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private List<SubtaskDTO> subtasks;

    public TaskDetailDTO() {
        subtasks = new ArrayList<>();
    }

    public List<SubtaskDTO> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<SubtaskDTO> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public String toString() {
        return super.toString() + "TaskDetailDTO [subtasks=" + subtasks + "]";
    }

    public class SubtaskDTO {
        private String id;
        private DisplayDTO objType;
        private String objId;
        private String objName;
        private DisplayDTO action;
        private Integer priority;
        private String startDateTime;
        private String endDateTime;
        private DisplayDTO state;
        private Long timeout;
        private String msg;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public DisplayDTO getObjType() {
            return objType;
        }

        public void setObjType(DisplayDTO objType) {
            this.objType = objType;
        }

        public String getObjId() {
            return objId;
        }

        public void setObjId(String objId) {
            this.objId = objId;
        }

        public String getObjName() {
            return objName;
        }

        public void setObjName(String objName) {
            this.objName = objName;
        }

        public DisplayDTO getAction() {
            return action;
        }

        public void setAction(DisplayDTO action) {
            this.action = action;
        }

        public Integer getPriority() {
            return priority;
        }

        public void setPriority(Integer priority) {
            this.priority = priority;
        }

        public String getStartDateTime() {
            return startDateTime;
        }

        public void setStartDateTime(String startDateTime) {
            this.startDateTime = startDateTime;
        }

        public String getEndDateTime() {
            return endDateTime;
        }

        public void setEndDateTime(String endDateTime) {
            this.endDateTime = endDateTime;
        }

        public DisplayDTO getState() {
            return state;
        }

        public void setState(DisplayDTO state) {
            this.state = state;
        }

        public Long getTimeout() {
            return timeout;
        }

        public void setTimeout(Long timeout) {
            this.timeout = timeout;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        @Override
        public String toString() {
            return "SubtaskDTO [id=" + id + ", objType=" + objType + ", objId=" + objId + ", objName=" + objName
                    + ", action=" + action + ", priority=" + priority + ", startDateTime=" + startDateTime
                    + ", endDateTime=" + endDateTime + ", state=" + state + ", timeout=" + timeout + ", msg=" + msg
                    + "]";
        }

    }
}
