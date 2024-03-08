#version 450

#define PI 3.1415926535897932384626433832795
#define MAX_TEXTURES 4		// save some space in the push constants by hard-wiring this

layout(location = 0) out vec4 color;

// interpolated position and direction of ray in world space
layout(location = 0) in vec3 p;
layout(location = 1) in vec3 d;

//push constants block
layout( push_constant ) uniform constants
{
	mat4 invView; // camera-to-world
	vec4 proj; // (near, far, aspect, fov)
	float time;

} pc;

// Set an extremely high global t value so that the first time we calculate a t value for ray and sphere it will be closer
float lowestT = 100000000;

layout(binding = 0) uniform sampler2D textures[ MAX_TEXTURES ];

// Material properties
//vec3 bg_color = vec3(0.10,0.00,0.05);





vec4 trial(float disc, vec3 d, vec3 pm,float prod, float norm, vec4 color, int tex, vec3 center){

    vec3 dir = normalize(d);
    if( disc >= 0.0) {
        // determine intersection point
        float t1 = 0.5 * (-prod - sqrt(disc));
        float t2 = 0.5 * (-prod + sqrt(disc));
        float tmin, tmax;
        float t;

        if(t1 < t2) {
            tmin = t1;
            tmax = t2;
        } else {
            tmin = t2;
            tmax = t1;
        }
        if(tmax > 0.0) {
            t = (tmin > 0) ? tmin : tmax;

            // if the depth is closer than the current closest then reset the lowestT value
            if (t < lowestT) {
                lowestT = t;
            }else {
                return color;
            }
            vec3 ipoint = pm + lowestT*(dir);
            vec3 normal = normalize(ipoint);
            
            // determine texture coordinates in spherical coordinates
            
            // First rotate about x through 90 degrees so that y is up.
            normal.z = -normal.z;
            normal = normal.xzy;
            

            float phi = acos(normal.z);
            float theta;
            if(abs(normal.x) < 0.001) {
                theta = sign(normal.y)*PI*0.5; 
            } else {
                theta = atan(normal.y, normal.x); 
            }
            // normalize coordinates for texture sampling. 
            // Top-left of texture is (0,0) in Vulkan, so we can stick to spherical coordinates

            // Background has only ambient
            // Sun has only ambient
            // Earth has full phong
            // Moon has full phong

                // Background
                if (tex == 0){
                    color = texture(textures[tex], vec2(1.0+0.5*(theta)/PI, (phi)/PI ));
                    vec3 lightColor = vec3(1.0,1.0,1.0);

                    // Ambient
                    float ambientStrength = 3.0;
                    vec3 ambient = vec3(ambientStrength * lightColor);
                    color = vec4(ambient, 1.0) * color;
                     
                }
                // Sun
                else if (tex == 1){
                    
                    color = texture(textures[ tex], vec2(1.0+0.5*(pc.time * 2 * PI * 27 / 324 + theta)/PI, (phi)/PI ));
                    vec3 lightColor = vec3(1.0,1.0,1.0);
                    vec3 lightPos = vec3(0.0, 0.0, 0.0);
                    vec3 lightDir = normalize(lightPos - ipoint);
                    vec3 normalLight = -normalize(ipoint - center);

                    // Ambient
                    float ambientStrength = 0.5;
                    vec3 ambient = vec3(ambientStrength * lightColor);
                    

                } 
                // Earth
                else if (tex == 2){
                    // Earth axial period is 27 times less than moon and sun
                    color = texture(textures[tex], vec2(1.0+0.5*(-pc.time * 2 * PI * 27 / 324 * 27  + theta)/PI, (phi)/PI ));
                
                    vec3 lightColor = vec3(1.0,1.0,1.0);
                    vec3 lightPos = vec3(0.0, 0.0, 0.0);
                    vec3 lightDir = normalize(lightPos - ipoint);
                    vec3 normalLight = -normalize(ipoint - center);

                    // Ambient
                    float ambientStrength = 0.1;
                    vec3 ambient = vec3(ambientStrength * lightColor);

                    // Diffuse
                    float diff = max(dot(normalLight, lightDir), 0.0);
                    vec3 diffuse = vec3(diff * lightColor);

                    // Specular
                    float specularStrength = 5.0;
                    vec3 viewDir = normalize(p - ipoint);
                    vec3 reflectDir = reflect(-lightDir, normalLight);

                    //float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32);
                    float spec = pow(max(dot(lightDir, normalLight), 0.0), 32);
                    spec *= max(dot(normalLight, lightDir), 0.0);

                    vec3 specular = specularStrength * spec * lightColor;
                    color = vec4(specular + diffuse + ambient, 1.0) * color;
                }
                // Moon
                else if (tex == 3){
                    color = texture(textures[tex], vec2(1.0+0.5*(-pc.time * 2 * PI * 27 / 324 + theta)/PI, (phi)/PI ));
                
                    vec3 lightColor = vec3(1.0,1.0,1.0);
                    vec3 lightPos = vec3(0.0, 0.0, 0.0);
                    vec3 lightDir = normalize(lightPos - ipoint);
                    vec3 normalLight = -normalize(ipoint - center);

                    // Ambient
                    float ambientStrength = 0.1;
                    vec3 ambient = vec3(ambientStrength * lightColor);

                    // Diffuse
                    float diff = max(dot(normalLight, lightDir), 0.0);
                    vec3 diffuse = vec3(diff * lightColor);

                    // Specular
                    float specularStrength = 5.0;
                    vec3 viewDir = normalize(p - ipoint);
                    vec3 reflectDir = reflect(-lightDir, normalLight);

                    //float spec = pow(max(dot(viewDir, reflectDir), 0.0), 32);
                    float spec = pow(max(dot(lightDir, normalLight), 0.0), 32);
                    spec *= max(dot(normalLight, lightDir), 0.0);

                    vec3 specular = specularStrength * spec * lightColor;
                    color = vec4(specular + diffuse + ambient, 1.0) * color;
                }


        }

    }
    return color;
} 


void main() {

    // Calculate the center and radii of the spheres
    float centerEarthx =  1.7 * sin(pc.time * 2 * PI / 324); 
    float centerEarthz = 1.7 * cos(pc.time * 2 * PI/ 324);
    vec3 centerEarth = vec3(centerEarthx, 0.0, centerEarthz);
    float radiusEarth = 0.05;

    vec3 centerMoon = vec3(centerEarth.x + 0.5 * sin(pc.time * 2 * PI * 27 / 324), centerEarth.y , centerEarth.z +  0.5 * cos(pc.time * 2 * PI * 27 / 324));
    float radiusMoon = 0.02;

    vec3 centerSun = vec3(0.0,0.0,0.0);
    float radiusSun = 0.2;

    vec3 centerStars = vec3(0.0, 0.0, 0.0);
    float radiusStars = 1000.0;

    
    // Normalize direction of light ray
    vec3 dir = normalize(d);

    // Change position of ray for spheres
    vec3 pEarth = p - centerEarth;
    vec3 pSun = p - centerSun;
    vec3 pMoon = p - centerMoon;  
    vec3 pStars = p - centerStars;  

    // Change prod value and normal values for ray
    float prodEarth = 2.0 * dot(pEarth,dir);
    float normpEarth = length(pEarth);

    float prodMoon = 2.0 * dot(pMoon,dir);
    float normpMoon = length(pMoon);

    float prodSun = 2.0 * dot(pSun,dir);
    float normpSun = length(pSun);

    float prodStars = 2.0 * dot(pStars,dir);
    float normpStars = length(pStars);

    // Find discriminants
    float discriminantEarth = prodEarth*prodEarth -4.0*(-radiusEarth  + normpEarth*normpEarth);
    float discriminantMoon = prodMoon*prodMoon -4.0*(-radiusMoon  + normpMoon*normpMoon);
    float discriminantSun = prodSun*prodSun -4.0*(-radiusSun  + normpSun*normpSun);
    float discriminantStars = prodStars*prodStars -4.0*(-radiusStars  + normpStars*normpStars);

    // Render spheres
    color = trial(discriminantEarth, d, pEarth, prodEarth, normpEarth, color, 2, centerEarth);
    color = trial(discriminantMoon, d, pMoon, prodMoon, normpMoon, color, 3, centerMoon);
    color = trial(discriminantSun, d, pSun, prodSun, normpSun, color, 1, centerSun);
    color = trial(discriminantStars, d, pStars, prodStars, normpStars, color, 0, centerStars);

}
