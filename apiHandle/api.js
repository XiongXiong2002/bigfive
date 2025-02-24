// 📌 /js/api.js
const BASE_URL = "http://localhost:8080/api"; // 你的后端 API 地址

// 统一处理 API 请求
async function fetchAPI(endpoint, method, body = null) {
    const options = {
        method: method,
        headers: { "Content-Type": "application/json" }
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    try {
        const response = await fetch(`${BASE_URL}${endpoint}`, options);
        const contentType = response.headers.get("content-type");

        // 🔹 检查是否是 JSON 响应
        if (contentType && contentType.includes("application/json")) {
            const jsonData = await response.json(); 
            return jsonData;
        }

        // 🔹 如果不是 JSON，获取文本内容
        const rawText = await response.text();
        return rawText;
    } catch (error) {
        console.error(" API Error:", error.message);
        return { error: error.message };
    }
}

// 🎯 获取测试结果
async function getResult(score) {
    return await fetchAPI("/questions/result", "POST", score);
}

async function getComplexResult(score){
    return await fetchAPI("/questions/getComplexResult", "POST", score);
}

// 🎯 获取题目
async function getQuestions(type) {
    return await fetchAPI("/questions/random", "POST",{state:type});
}

async function validate(inputAdmin){
    return await fetchAPI("/admin/validate","POST",inputAdmin)
}

async function getAll(inputAdmin) {
    return await fetchAPI("/admin/getAll","POST",inputAdmin)
    
}

async function addElement(elements) {
    return await fetchAPI("/admin/add","POST",elements)
    
}

async function deleteQuestion(index) {
    return await fetchAPI("/admin/delete","POST",index)
    
}

// ✅ 导出 API 供其他页面使用
export { getResult, getQuestions,getComplexResult,validate,getAll,addElement,deleteQuestion }