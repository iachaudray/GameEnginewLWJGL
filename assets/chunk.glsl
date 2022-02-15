#type vertex
#version 410
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 normal;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform sampler2D depthTex;
out vec3 fragNormal;
out float depthValue;
void main() {
    vec2 TexCoords = ((projectionMatrix * viewMatrix * modelMatrix) * vec4(aPos, 1.0)).xy;
    float depthVal = texture(depthTex, TexCoords).r;
    gl_Position = (projectionMatrix * viewMatrix * modelMatrix) *   vec4(aPos, 1.0);
    fragNormal = normal;
    depthVal = depthValue;
}

#type fragment
#version 410
in vec3 fragNormal;
in float depthValue;
out vec4 color;
uniform vec3 sunLightDirection;
void main() {
    float ambientStrength = 0.3;
    float x = max(dot(fragNormal, sunLightDirection), 0);
    vec3 light = (ambientStrength + x) * vec3(1.0, 0.0, 0.0);
    light = light * depthValue;
    color = vec4(light, 1.0);

}