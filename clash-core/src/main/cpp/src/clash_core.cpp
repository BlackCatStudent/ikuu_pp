#include <string>

namespace clash {

class ClashCore {
public:
    ClashCore();
    ~ClashCore();
    
    bool start(const std::string& configPath);
    void stop();
    bool isRunning() const;
    std::string getVersion() const;
    
private:
    bool running_;
};

}