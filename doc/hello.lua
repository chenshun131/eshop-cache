-- 获取访问参数中的 productId
local uri_args = ngx.req.get_uri_args()
local productId = uri_args["productId"]

-- 通过对 productId 的 hash 计算，获取对应服务器地址
local host = { "172.18.56.11:8080/hello1", "172.18.56.11:8080/hello2" }
local hash = ngx.crc32_long(productId)
hash = (hash % 2) + 1
backend = "http://" .. host[hash]

-- 将访问路径拼接成相应的访问地址
local method = uri_args["method"]
local fullUri = backend .. "/" .. method .. "?productId=" .. productId

-- 重定向进行访问
local http = require("resty.http")
local httpc = http.new()
local resp, err = httpc:request_uri(fullUri, {
    method = "GET"
})

-- 访问失败报错
if not resp then
    ngx.say("request error :", err)
    return
end

-- 访问成功返回对应信息
ngx.say(resp.body)

-- 关闭访问请求
httpc:close()
