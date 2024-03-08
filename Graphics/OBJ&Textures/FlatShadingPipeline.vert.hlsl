struct VSIn {
    float3 position : POSITION0;
    float2 baseColorUV : TEXCOORD0;
    float3 normal : NORMAL;
};

struct VSOut {
    float4 position : SV_POSITION;
    float2 baseColorUV : TEXCOORD0;
    float3 worldNormal : NORMAL;
    float3 viewDir : TEXCOORD1;
    //int m;
    //int AO;
    //int perlin;
    //int base;
};

struct ViewProjectionBuffer {
    float4x4 viewProjection;
};

ConstantBuffer <ViewProjectionBuffer> vpBuff: register(b0, space0);

struct PushConsts
{
    float4x4 model;
    int m;
    int perlin;
    int AO;
    int baseOn;
    float3 camView;
};

[[vk::push_constant]]
cbuffer {
    PushConsts pushConsts;
};

VSOut main(VSIn input) {
    VSOut output;

    float4x4 mvpMatrix = mul(vpBuff.viewProjection, pushConsts.model);
    // float4 N = mul(mvpMatrix, float4(input.normal, 0)); // add
    // float4 P = mul(mvpMatrix, float4(input.position.xyz, 1)); // add
    // output.viewDir = -P.xyz / P.w; //add
    output.position = mul(mvpMatrix, float4(input.position, 1.0));
    output.baseColorUV = input.baseColorUV;
    output.worldNormal = mul(pushConsts.model, float4(input.normal, 0.0f)).xyz;

    output.viewDir = normalize(float3(1.0,1.0,1.0) - input.position.xyz);


    //output.m = pushConsts.m;
    //output.perlin = pushConsts.perlin;
    //output.base = pushConsts.base;

    return output;
}