

package me.zhengjie.result;


import java.io.Serializable;

public class ResultModel<T> implements Serializable {
    private Integer code;
    private Long count;
    private String msg;
    private T data;

    public ResultModel() {
    }

    public ResultModel(Integer code, Long count, String msg) {
        this.msg = msg;
        this.code = code;
        this.count = count;
    }
    public ResultModel(Integer code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public boolean onNull() {
        return this.data == null;
    }

    public boolean isFlag() {
        return this.code != null && (this.code == 0 || this.code == 5);
    }

    public ResultModel<T> flashData(T data) {
        this.data = data;
        return this;
    }
    public ResultModel<T> flashDataV2(T data, Long count) {
        this.data = data;
        this.count = count;
        return this;
    }

    public ResultModel<T> flashMessage(String msg) {
        this.msg = msg;
        return this;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public T getData() {
        return this.data;
    }

    public void setCode(final Integer code) {
        this.code = code;
    }

    public void setMsg(final String msg) {
        this.msg = msg;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ResultModel)) {
            return false;
        } else {
            ResultModel<?> other = (ResultModel)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label47: {
                    Object this$code = this.getCode();
                    Object other$code = other.getCode();
                    if (this$code == null) {
                        if (other$code == null) {
                            break label47;
                        }
                    } else if (this$code.equals(other$code)) {
                        break label47;
                    }

                    return false;
                }

                Object this$msg = this.getMsg();
                Object other$msg = other.getMsg();
                if (this$msg == null) {
                    if (other$msg != null) {
                        return false;
                    }
                } else if (!this$msg.equals(other$msg)) {
                    return false;
                }

                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ResultModel;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        Object $msg = this.getMsg();
        result = result * 59 + ($msg == null ? 43 : $msg.hashCode());
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    public String toString() {
        return "ResultModel(code=" + this.getCode() + ", msg=" + this.getMsg() + ", data=" + this.getData() + ")";
    }
}
