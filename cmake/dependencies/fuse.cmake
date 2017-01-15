if(ANDROID)
    set(FUSE_INCLUDE_DIR
        ${THIRD_PARTY_FUSE_DIR}/${ANDROID_ABI}/include)
    set(FUSE_LIBRARY
        ${THIRD_PARTY_FUSE_DIR}/${ANDROID_ABI}/lib/libfuse.a)
endif()

find_package(Fuse REQUIRED)

set(MBP_FUSE_INCLUDES ${FUSE_INCLUDE_DIR})
set(MBP_FUSE_LIBRARIES ${FUSE_LIBRARIES})
