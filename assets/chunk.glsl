#type vertex
#version 410
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 normal;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform mat4 lightProjection;
uniform mat4 lightView;
out VS_OUT {
    vec3 FragPos;
    vec3 Normal;
    vec4 FragPosLightSpace;
} vs_out;
void main() {
    vs_out.FragPos = vec3(modelMatrix * vec4(aPos, 1.0));
    mat4 lightSpaceMatrix = ((lightProjection * lightView));
    vs_out.FragPosLightSpace = lightSpaceMatrix * vec4(vs_out.FragPos, 1.0);
    vs_out.Normal = normal;
    gl_Position = (projectionMatrix * viewMatrix * modelMatrix) *   vec4(aPos, 1.0);
}

#type fragment
#version 410

in VS_OUT {
    vec3 FragPos;
    vec3 Normal;
    vec4 FragPosLightSpace;
} vs_out;
out vec4 color;
uniform vec3 sunLightDirection;
uniform sampler2D depthTex;

float ShadowCalculation(vec4 fragPosLightSpace)
{
    // perform perspective divide
    vec3 projCoords = fragPosLightSpace.xyz / fragPosLightSpace.w;
    // transform to [0,1] range
    projCoords = projCoords * 0.5 + 0.5;
    // get closest depth value from light's perspective (using [0,1] range fragPosLight as coords)
    float closestDepth = texture(depthTex, projCoords.xy).r;
    // get depth of current fragment from light's perspective
    float currentDepth = projCoords.z;
    // check whether current frag pos is in shadow
    float shadow = currentDepth > closestDepth  ? 1.0 : 0.0;

    return shadow;
}



void main() {
    float ambientStrength = 0.3;
    float x = max(dot(vs_out.Normal, sunLightDirection), 0);
    float shadow = ShadowCalculation(vs_out.FragPosLightSpace);
    vec3 light = (ambientStrength + (1.0- shadow) + x) * vec3(1.0, 0.0, 0.0);

    color = vec4(light, 1.0);

}