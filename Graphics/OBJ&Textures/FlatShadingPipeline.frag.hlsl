#include "../ColorUtils.hlsl"

struct PSIn {
    float4 position : SV_POSITION;
    float2 baseColorUV : TEXCOORD0;
    float3 worldNormal : NORMAL;
    float3 viewDir : TEXCOORD1;
};

struct PSOut {
    float4 color : SV_Target0;
};

struct Material
{
    float4 color;
    int hasBaseColorTexture;
    int placeholder0;
    int placeholder1;
    int placeholder2;
};

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


sampler textureSampler : register(s1, space0);

ConstantBuffer <Material> material: register(b0, space1);




Texture2D perlinColorTexture : register(t1, space1);
Texture2D baseColorTexture : register(t2, space1);
Texture2D AOColorTexture : register(t3, space1);

PSOut main(PSIn input) {
    PSOut output;

    float3 color;
    float alpha;
    float3 perlincolor;

    color = material.color.rgb;
    alpha = material.color.a;

    if(pushConsts.baseOn == 1 ){
        color = baseColorTexture.Sample(textureSampler, float2(input.baseColorUV.x, input.baseColorUV.y));
    }

    if (pushConsts.perlin == 1){
        perlincolor = perlinColorTexture.Sample(textureSampler, float2(pow(2, 0) * input.baseColorUV.x, pow(2, 0) * input.baseColorUV.y) )/ pow(2, 0);
        for (int i = 1; i < 5; i++){
            perlincolor += perlinColorTexture.Sample(textureSampler, float2(pow(2, i) * input.baseColorUV.x, pow(2, i) * input.baseColorUV.y) )/ pow(2, i);
        }
        
        perlincolor = 0.5f * (1 + sin(pushConsts.m * 3.14159265* (input.baseColorUV.x + input.baseColorUV.y + perlincolor)));

        if (pushConsts.baseOn == 1){
            color *= perlincolor;
        }
        else{
            color = perlincolor;
        }
        
    }
    float ambient_strength = 0.25;
    if (pushConsts.AO == 1){
        ambient_strength *= AOColorTexture.Sample(textureSampler, input.baseColorUV);

    }

    

    
    float specular_strength = 2.0;
    float3 specular_power = 76.8;
    float3 light_color = (0.5,0.5,0.5);
    float3 light_position = (500, -100, 0);

    float3 N = normalize(input.worldNormal);
    float3 L = normalize(light_position - input.viewDir);
    float3 V = normalize(pushConsts.camView - input.viewDir);

    float3 R = reflect(-L, N);

    //ambient and diffuse
    float3 diff = max(dot(L,N), 0.0) * color;
    float3 ambient = ambient_strength * color;

    // make sure light doesn't show up on backside
    float dotter = max(dot(R,V), 0.0);
    if (dotter < 0){
        specular_strength = 0.0;
    }


    float3 specular = pow(dotter, specular_power) * specular_strength;


    //float4 color2 = float4((ambient + diff + specular) * light_color, alpha);

    float3 color2 = float3((ambient + diff + specular) * light_color);



    color2 = ApplyExposureToneMapping(color2);
    // Gamma correct
    color2 = ApplyGammaCorrection(color2); 

    output.color = float4(color2, alpha);
    //output.color = color2;
    return output;
}