package net.xnzn.photo.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;

import java.util.Map;
import java.util.TreeMap;

/**
 * 1
 * <p>
 * date 2024-02-20
 * author lzg
 */
public class ApiSignUtil {
    // 根据参数和密钥生成签名
    public static String generateSignature(Map<String, Object> params, String secretKey) {
        // 将参数按照字典顺序排序
        TreeMap<String, Object> sortedParams = new TreeMap<>(params);
        //移除value空值
        sortedParams.values().removeIf(ObjectUtil::isEmpty);
        sortedParams.remove("sign");
        // 拼接参数字符串
        StringBuilder paramString = new StringBuilder();
        for (Map.Entry<String, Object> entry : sortedParams.entrySet()) {
            paramString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        // 去除末尾的"&"
        String paramsString = StrUtil.removeSuffix(paramString.toString(), "&");

        // 使用HMAC-SHA256算法进行签名
        HMac mac = new HMac(HmacAlgorithm.HmacSHA256, secretKey.getBytes());
        return mac.digestHex(paramsString);
    }

    // 验证签名是否正确
    public static boolean verifySignature(Map<String, Object> params, String secretKey, String sign) {
        // 重新生成签名
        String generatedSignature = generateSignature(params, secretKey);
        // 验证签名是否一致
        return generatedSignature.equals(sign);
    }

    public static void main(String[] args) {
        // 示例参数和密钥
        Map<String, Object> params = new TreeMap<>();
        params.put("custId", "131232");
        params.put("companyId", "123");

        String secretKey = "a3411f2f74a8ebebe0f3fc95d30e4d1a";

        // 生成签名
        String signature = generateSignature(params, secretKey);
        System.out.println("Generated Signature: " + signature);

        // 验证签名
        boolean isValid = verifySignature(params, secretKey, signature);
        System.out.println("Is Valid Signature: " + isValid);
    }
}
