package com.cad.vr.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: Knox
 * @Date: 2019/12/1 12:30 PM
 * @Description: You Know
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientEvent implements Serializable {
    String id;
    String type;
    String x;
    String y;
}
